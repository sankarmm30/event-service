FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/event-service*.jar
ENV JAVA_OPTS=""

COPY ${JAR_FILE} app.jar

ENTRYPOINT exec java $JAVA_OPTS  -jar /app.jar