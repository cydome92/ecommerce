FROM docker.io/eclipse-temurin:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]