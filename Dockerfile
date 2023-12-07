FROM openjdk:17-jdk-oraclelinux8 AS build
WORKDIR /app
ADD . /app
RUN ./mvnw -X -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17.0.3_7-jre-jammy
COPY --from=build /app/target/*.jar /app/tutorials-backend.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/tutorials-backend.jar


#FROM maven:3.9.5-amazoncorretto-17
#
#WORKDIR /app
#COPY . .
#RUN mvn clean install
#
#CMD mvn spring-boot:run