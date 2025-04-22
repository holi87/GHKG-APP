FROM eclipse-temurin:17-jdk AS builder
RUN apt-get update && apt-get install -y git
WORKDIR /app
COPY pom.xml ./
COPY .mvn/ .mvn
COPY mvnw ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN git clone https://github.com/holi87/GHKG-APP.git . --depth=1 --branch master
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]