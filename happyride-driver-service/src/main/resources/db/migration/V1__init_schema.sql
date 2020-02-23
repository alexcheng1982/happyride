CREATE TABLE drivers(
  id varchar(36) NOT NULL PRIMARY KEY,
  created_at bigint NOT NULL,
  updated_at bigint NOT NULL,
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  mobile_phone_number varchar(255) NOT NULL
);

CREATE TABLE vehicles(
  id varchar(36) NOT NULL PRIMARY KEY,
  driver_id varchar(36) NOT NULL,
  make varchar(32) NOT NULL,
  mode varchar(32) NOT NULL,
  year int NOT NULL,
  registration varchar(32) NOT NULL,
  FOREIGN KEY (driver_id) REFERENCES drivers(id)
);