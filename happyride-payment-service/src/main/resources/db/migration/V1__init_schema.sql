CREATE SCHEMA IF NOT EXISTS happyride;

CREATE TABLE happyride.payments(
  id varchar(36) NOT NULL PRIMARY KEY,
  created_at bigint NOT NULL,
  updated_at bigint NOT NULL,
  trip_id varchar(36) NOT NULL,
  amount decimal(10,2) NOT NULL DEFAULT '0.00',
  state varchar(50) NOT NULL
);
