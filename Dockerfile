# Stage 1: Build the application using Maven wrapper
FROM openjdk:19-jdk AS build
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Ensure Maven wrapper has execution permissions
RUN chmod +x mvnw

# Run Maven build (without tests to speed up the process)
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:19-jdk
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose port 8080 and set the entrypoint
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
