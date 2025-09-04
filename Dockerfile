# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Download and install Maven
RUN curl -sSL https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.tar.gz | tar xzf - && \
    mv apache-maven-3.9.5 /opt/maven && \
    export PATH=$PATH:/opt/maven/bin

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN /opt/maven/bin/mvn dependency:go-offline -B

# Copy the source code
COPY src src

# Build the application
RUN /opt/maven/bin/mvn clean package -DskipTests

# Create a non-root user
RUN addgroup --system appuser && adduser --system --ingroup appuser appuser

# Change ownership of the app directory
RUN chown -R appuser:appuser /app

# Switch to the non-root user
USER appuser

# Expose the port the app runs on
EXPOSE 8080

# Set the startup command
CMD ["java", "-jar", "target/goal-tracker-1.0.0.jar"]
