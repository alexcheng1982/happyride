const falcor = require('falcor');
const jsong = require("falcor-json-graph");
const jsonGraph = require('./sample_json_graph.json');
const debug = require('./debug').debug;

const model = new falcor.Model({
  cache: jsonGraph
});


model.getValue(["passengersById", "p1", "name"]).then(debug);
model.getValue("passengersById.p1.name").then(debug);
model.getValue(["passengersById", "p1", "userAddresses", 0, 'address', 'addressLine']).then(debug);

model.setValue(jsong.pathValue(["passengersById", "p1", "name"], "new name")).then(debug);