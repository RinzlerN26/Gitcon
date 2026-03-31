# Development

FROM eclipse-temurin:21

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]

# Production

# FROM eclipse-temurin:21 AS build

# WORKDIR /app

# COPY .mvn/ .mvn

# COPY mvnw pom.xml ./

# RUN ./mvnw dependency:go-offline

# COPY src ./src

# RUN ./mvnw package -DskipTests

# FROM eclipse-temurin:21-jre

# WORKDIR /app

# COPY --from=build /app/target/*.jar app.jar

# EXPOSE 8080

# CMD ["java", "-jar", "app.jar"]