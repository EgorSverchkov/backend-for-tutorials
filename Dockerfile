#
# Build stage
#
FROM openjdk:17-jdk-oraclelinux8 AS build
ENV HOME=/app
RUN mkdir $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:17.0.3_7-jre-jammy
ARG JAR_FILE=/app/target/*.jar
COPY --from=build $JAR_FILE /app/tutorials-backend.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/tutorials-backend.jar