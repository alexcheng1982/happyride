CREATE SCHEMA IF NOT EXISTS happyride;

CREATE TABLE happyride.drivers (
  id varchar(36) NOT NULL PRIMARY KEY,
  created_at bigint NOT NULL,
  updated_at bigint NOT NULL,
  name varchar(255) NOT NULL,
  email varchar(255) NULL,
  mobile_phone_number varchar(255) NOT NULL,
  state varchar(30) NOT NULL
);

CREATE TABLE happyride.vehicles (
  id varchar(36) NOT NULL PRIMARY KEY,
  created_at bigint NOT NULL,
  updated_at bigint NOT NULL,
  driver_id varchar(36) NOT NULL,
  make varchar(60) NOT NULL,
  mode varchar(60) NOT NULL,
  year int NOT NULL,
  registration varchar(32) NOT NULL,
  FOREIGN KEY (driver_id) REFERENCES happyride.drivers(id)
);