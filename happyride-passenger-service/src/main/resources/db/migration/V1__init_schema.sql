CREATE SCHEMA IF NOT EXISTS happyride;

CREATE TABLE happyride.passengers (
  id varchar(36) NOT NULL PRIMARY KEY,
  created_at bigint NOT NULL,
  updated_at bigint NOT NULL,
  name varchar(255) NOT NULL,
  email varchar(255) NULL,
  mobile_phone_number varchar(255) NOT NULL
);

CREATE TABLE happyride.user_addresses (
  id varchar(36) NOT NULL PRIMARY KEY,
  passenger_id varchar(36) NOT NULL,
  name varchar(255) NOT NULL,
  address_id varchar(36) NOT NULL,
  FOREIGN KEY (passenger_id) REFERENCES happyride.passengers(id)
);