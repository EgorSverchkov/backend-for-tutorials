#FROM maven:3.8.4-openjdk-17 as builder
#WORKDIR /app
#COPY . /app/.
#RUN ./mvnw -f /app/pom.xml clean
#RUN ./mvnw package -f /app/pom.xml -Dmaven.test.skip=true
#
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /app
#COPY --from=builder /app/target/*.jar /app/*.jar
#EXPOSE 8181
#ENTRYPOINT ["java", "-jar", "/app/*.jar"]
#

FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY ./src ./src
RUN ./mvnw clean
RUN ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]