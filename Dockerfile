FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM build

WORKDIR /app

COPY --from=build /app/target/foodgram-backend-0.0.1-SNAPSHOT.jar app.jar

RUN addgroup --system spring && adduser --system --group spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
