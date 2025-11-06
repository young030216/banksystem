# =============================
# Build stage
# =============================
FROM gradle:8.5-jdk21 AS builder

# Set work directory
WORKDIR /home/gradle/project

# copy config
COPY build.gradle settings.gradle ./

# download dependencies
RUN gradle clean build --no-daemon || return 0

# copy source code
COPY src ./src

# construct jar
RUN gradle bootJar --no-daemon

# =============================
# run stage
# =============================
FROM openjdk:21-jdk-slim

# Create non-root user and group
RUN groupadd --system spring && useradd --system --create-home --gid spring springuser

# Set work directory
WORKDIR /app

# copy jar from build stage
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
RUN chown -R springuser:spring /app

# switch to non-root user
USER springuser:spring

# expose port
EXPOSE 8080

# run jar
ENTRYPOINT ["java", "-jar", "app.jar"]

