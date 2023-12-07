#
# Build stage
#
FROM openjdk:17-jdk-oraclelinux8 AS build
RUN mkdir /app
WORKDIR /app
ADD . /app
RUN ./mvnw -X -f /app/pom.xml clean package -Dmaven.test.skip=true

#
# Package
#
FROM eclipse-temurin:17.0.3_7-jre-jammy
COPY --from=build /app/target/*.jar /app/tutorials-backend.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/tutorials-backend.jar