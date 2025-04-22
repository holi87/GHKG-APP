FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY pom.xml ./
COPY .mvn/ .mvn
COPY mvnw ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN git init \
 && git config user.email "ci@ghkg" \
 && git config user.name "GHKG CI" \
 && git remote add origin https://github.com/holi87/GHKG-APP.git \
 && git fetch --depth=1 origin master \
 && git checkout master
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]