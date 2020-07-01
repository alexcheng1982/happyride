const falcorExpress = require("falcor-express");
const Router = require("falcor-router");
const jsong = require("falcor-json-graph");
const axios = require("axios").default;
const Promise = require("bluebird");
const get = require("lodash.get");
const express = require("express");
const bodyParser = require("body-parser");
const app = express();
app.use(bodyParser.urlencoded({ extended: false }));

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

function addUserAddress(passengerId, addressName, addressId) {
  return passengerServiceClient
    .post(`/${passengerId}/addresses`, {
      name: addressName,
      addressId: addressId,
    })
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
          "passengersById[{keys:ids}]['name', 'email', 'mobilePhoneNumber', 'userAddresses']",
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
        get: async function (pathSet) {
          const passenger = await getPassenger(pathSet[1]);
          return {
            path: pathSet,
            value: jsong.ref([
              "addressesById",
              get(passenger, ["userAddresses", pathSet[3], "addressId"], null),
            ]),
          };
        },
      },
      {
        route: "passengersById.addUserAddress",
        call: function (callPath, args) {
          return addUserAddress(args[0], args[1], args[2]).then(
            (response) => {
              return {
                jsonGraph: {},
                paths: [],
                invalidated: [["passengersById", response.id, "userAddresses"]],
              };
            }
          );
        },
      },
    ]);
  })
);

app.use(express.static(__dirname + "/"));

app.listen(3000);
