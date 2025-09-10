# ===== Build stage =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /src

# Copia il progetto "demo" (pom.xml + sorgenti)
COPY demo/pom.xml .
COPY demo/src ./src

# Compila senza test
RUN mvn -DskipTests=true clean package

# ===== Runtime stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia il jar buildato
COPY --from=build /src/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Render setta $PORT automaticamente → Spring lo legge da application.properties
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
