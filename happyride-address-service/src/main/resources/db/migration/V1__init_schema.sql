CREATE SEQUENCE hibernate_sequence START 1;

DROP TABLE IF EXISTS area;
CREATE TABLE area (
  id SERIAL PRIMARY KEY,
  level int NOT NULL,
  parent_code bigint NOT NULL DEFAULT '0',
  area_code bigint NOT NULL DEFAULT '0',
  zip_code varchar(6) NOT NULL DEFAULT '000000',
  city_code varchar(6) NOT NULL DEFAULT '',
  name varchar(50) NOT NULL DEFAULT '',
  short_name varchar(50) NOT NULL DEFAULT '',
  merger_name varchar(50) NOT NULL DEFAULT '',
  pinyin varchar(30) NOT NULL DEFAULT '',
  lng decimal(10,6) NOT NULL DEFAULT '0.000000',
  lat decimal(10,6) NOT NULL DEFAULT '0.000000'
);

DROP TABLE IF EXISTS address;
CREATE TABLE address(
  id varchar(36) NOT NULL,
  created_at bigint NOT NULL,
  updated_at bigint NOT NULL,
  area_id int NOT NULL,
  address_line varchar(256) NOT NULL,
  lng decimal(10,6) NOT NULL DEFAULT '0.000000',
  lat decimal(10,6) NOT NULL DEFAULT '0.000000',
  PRIMARY KEY (id),
  FOREIGN KEY (area_id) REFERENCES area(id)
);