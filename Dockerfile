FROM eclipse-temurin:17-jdk AS fetch
RUN apt-get update && apt-get install -y git
WORKDIR /tmp/source
RUN git clone https://github.com/holi87/GHKG-APP.git . --depth=1 --branch master

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY --from=fetch /tmp/source/. .
RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
