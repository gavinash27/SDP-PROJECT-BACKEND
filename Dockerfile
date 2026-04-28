# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build project
RUN ./mvnw clean package -DskipTests

# Render provides dynamic PORT
ENV PORT=8080

# Expose correct port
EXPOSE 8080

# Run app with dynamic port
CMD sh -c "java -jar target/*.jar --server.port=$PORT"