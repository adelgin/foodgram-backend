# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Копируем pom.xml и исходники
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM build

WORKDIR /app

# Копируем JAR из stage build
COPY --from=build /app/target/foodgram-backend-0.0.1-SNAPSHOT.jar app.jar

# Создаем не-root пользователя
RUN addgroup --system spring && adduser --system --group spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
