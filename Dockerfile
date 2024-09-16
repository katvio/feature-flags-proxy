# Single stage build using Maven image

FROM maven:3.9.8-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# # Create a user and group to run the application
# RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# # Change ownership of the app directory
# RUN chown -R appuser:appgroup /app

# # Switch to the non-root user
# USER appuser

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/target/feature-flags-proxy-1.0-SNAPSHOT.jar"]

# Expose the port the application runs on
EXPOSE 8080

# Health check to ensure the container is running properly
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
