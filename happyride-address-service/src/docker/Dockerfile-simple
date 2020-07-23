FROM adoptopenjdk/openjdk8:jre8u262-b10-alpine

ADD target/happyride-address-service-1.0.0-SNAPSHOT.jar /opt/app.jar
ENTRYPOINT [ "java", "-jar", "/opt/app.jar" ]