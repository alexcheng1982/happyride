const falcorExpress = require("falcor-express");
const Router = require("falcor-router");
const jsong = require("falcor-json-graph");
const axios = require("axios").default;
const Promise = require("bluebird");
const express = require("express");
const app = express();

const passengerServiceClient = axios.create({
  baseURL: process.env.PASSENGER_SERVICE_URL,
});

const addressServiceClient = axios.create({
  baseURL: process.env.ADDRESS_SERVICE_URL,
});

function getPassenger(passengerId) {
  return passengerServiceClient
    .get("/" + passengerId)
    .then((response) => response.data);
}

function getAddress(addressId) {
  return addressServiceClient
    .get("/address/" + addressId)
    .then((response) => response.data);
}

function toEntityJsonGraph(mapKey, ids, props, promiseCreator) {
  return Promise.map(ids, (id) => {
    return promiseCreator(id).catch((error) => ({
      id: id,
      $error: error,
    }));
  }).then((entities) => {
    const jsonGraph = {},
      entityMap = (jsonGraph[mapKey] = {});
    entities.forEach((entity) => {
      if (entity.$error) {
        entityMap[entity.id] = jsong.error(entity.$error.message);
      } else {
        const p = (entityMap[entity.id] = {});
        props.forEach((prop) => {
          p[prop] = jsong.atom(entity[prop]);
        });
      }
    });
    return { jsonGraph: jsonGraph };
  });
}

app.use(
  "/model.json",
  falcorExpress.dataSourceRoute(function (req, res) {
    return new Router([
      {
        route:
          "addressesById[{keys:ids}]['addressLine', 'lat', 'lng', 'areaId']",
        get: function (pathSet) {
          return toEntityJsonGraph(
            "addressesById",
            pathSet.ids,
            pathSet[2],
            getAddress
          );
        },
      },
      {
        route:
          "passengersById[{keys:ids}]['name', 'email', 'mobilePhoneNumber']",
        get: function (pathSet) {
          return toEntityJsonGraph(
            "passengersById",
            pathSet.ids,
            pathSet[2],
            getPassenger
          );
        },
      },
      {
        route: "passengersById[{keys}].userAddresses[{integers}]['address']",
        get: function (pathSet) {
          return getPassenger(pathSet[1]).then((passenger) => {
            return {
              path: pathSet,
              value: jsong.ref([
                "addressesById",
                passenger.userAddresses[pathSet[3]].addressId,
              ]),
            };
          });
        },
      },
    ]);
  })
);

app.use(express.static(__dirname + "/"));

app.listen(3000);
