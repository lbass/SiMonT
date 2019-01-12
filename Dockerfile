FROM openjdk:8-jdk-alpine
RUN mkdir -p /logs
ARG JAR_FILE=target/simont-*-jar-with-dependencies.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-cp", "./app.jar", "com.lbass.simont.SimontApplication"]