# Stage 1, BUILDS maven project in a LARGE JDK based IMAGE
FROM maven:3.9-eclipse-temurin-21-jammy as build-stage
# set the working directory in the container
WORKDIR /app/api-gateway
# copy pom.xml
COPY pom.xml .
# Download dependencies:
RUN mvn dependency:go-offline
# Copy the source code:
COPY src ./src 
# build the application:
RUN mvn clean package 
#-DskipTests

# Stage 2: Create the RUNTIME JRE image
# create efficient and lightweight image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app/api-gateway
# copy the built JAR file from the build stage
COPY --from=build-stage /app/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# set the entry point to run the application:
ENTRYPOINT ["java", "-jar", "app.jar"]