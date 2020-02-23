#! /bin/bash -e

for schema in happyride_ride_service ;
do
  user=${schema}_user
  password=${schema}_password
  cat >> /docker-entrypoint-initdb.d/5.schema-per-service.sql <<END
  CREATE USER '${user}'@'%' IDENTIFIED BY '$password';
  create database $schema;
  GRANT ALL PRIVILEGES ON $schema.* TO '${user}'@'%' WITH GRANT OPTION;
  USE $schema;
END
    cat /docker-entrypoint-initdb.d/template >> /docker-entrypoint-initdb.d/5.schema-per-service.sql
done
