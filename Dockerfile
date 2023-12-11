FROM openjdk:18-jdk-oraclelinux8 AS build

WORKDIR /app

COPY target/*.jar /app/application.jar

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]

