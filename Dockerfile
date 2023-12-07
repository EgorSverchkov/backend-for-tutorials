FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY ./src ./src
CMD ["mvn", "-X", "clean"]
CMD ["mvn", "package", "-Dmaven.test.skip=true", "clean"]

FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]