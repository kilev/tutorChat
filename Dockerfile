FROM bellsoft/liberica-openjdk-centos:11.0.3
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
