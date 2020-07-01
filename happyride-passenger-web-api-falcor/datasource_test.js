const falcor = require('falcor');
const jsonGraph = require('./sample_json_graph.json');
const debug = require('./debug').debug;

const dataSource = new falcor.Model({
  cache: jsonGraph
}).asDataSource();

dataSource.get(["passengersById", "p1", "userAddresses", 0, 'address', 'addressLine']).subscribe(debug);