# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# 🔥 FIX: Give execute permission to mvnw (YOUR ERROR FIX)
RUN chmod +x mvnw

# Build project
RUN ./mvnw clean package -DskipTests

# Render uses dynamic PORT → must match Spring Boot
EXPOSE 8084

# Run app
CMD sh -c "java -jar target/*.jar"