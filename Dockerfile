#
# BUILD STAGE
#
FROM eclipse-temurin:17-jdk AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true

#
# PACKAGE STAGE
#
FROM openjdk:17-jre-slim
COPY --from=build /usr/src/app/target/tutorials-backend.jar /usr/app/tutorials-backend.jar
EXPOSE 8080
CMD ["java","-jar","/usr/app/tutorials-backend.jar"]