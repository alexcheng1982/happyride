# Address Service Client

To view the API doc, start the Docker container first,

```shell script
docker run --rm -p 8800:8080 \
  -v $(pwd)/src/main/resources/openapi.yml:/openapi.yml \
  -e SWAGGER_JSON=/openapi.yml \
  swaggerapi/swagger-ui
```

then access `http://localhost:8800`.