FROM amazoncorretto:24
EXPOSE 8080
ARG JAR_FILE=target/tracker-platform-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} tracker-platform.jar
ENTRYPOINT ["java", "-jar", "/tracker-platform.jar"]
