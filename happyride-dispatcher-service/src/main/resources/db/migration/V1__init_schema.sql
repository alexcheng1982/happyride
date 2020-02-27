CREATE SCHEMA IF NOT EXISTS happyride;

CREATE TABLE dispatches(
  id varchar(36) NOT NULL PRIMARY KEY,
  trip_id varchar(36) NOT NULL,
  start_pos_lng decimal(10,6) NOT NULL DEFAULT '0.000000',
  start_pos_lat decimal(10,6) NOT NULL DEFAULT '0.000000'
);

CREATE TABLE trip_acceptances(
  id varchar(36) NOT NULL PRIMARY KEY,
  dispatch_id varchar(36) NOT NULL,
  timestamp bigint not null,
  current_pos_lng decimal(10,6) NOT NULL DEFAULT '0.000000',
  current_pos_lat decimal(10,6) NOT NULL DEFAULT '0.000000',
  state varchar(30) NOT NULL,
  FOREIGN KEY (dispatch_id) REFERENCES dispatches(id)
);