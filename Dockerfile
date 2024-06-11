# Fase 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copiamos el archivo pom.xml y descargamos las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copiamos el resto del código fuente y construimos el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Creación de la imagen de JAR
FROM openjdk:21 AS jar
WORKDIR /app
# Copiamos el JAR del contenedor de construcción
COPY --from=build /app/target/*.jar app.jar

# Fase 3: Ejecución
FROM openjdk:21
WORKDIR /app
# Copiamos el JAR del contenedor de creación de JAR
COPY --from=jar /app/app.jar app.jar
# Exponemos el puerto en el que correrá la aplicación
EXPOSE 8080
# Definimos el comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
