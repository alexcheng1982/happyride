CREATE SCHEMA IF NOT EXISTS happyride;

CREATE TABLE happyride.trips(
  id varchar(36) NOT NULL PRIMARY KEY,
  passenger_id varchar(36) NOT NULL,
  passenger_name varchar(255),
  driver_id varchar(36),
  driver_name varchar(255),
  start_pos_lat decimal(10,6) NOT NULL DEFAULT '0.000000',
  start_pos_lng decimal(10,6) NOT NULL DEFAULT '0.000000',
  end_pos_lat decimal(10,6) NOT NULL DEFAULT '0.000000',
  end_pos_lng decimal(10,6) NOT NULL DEFAULT '0.000000',
  state varchar(50) NOT NULL
);
