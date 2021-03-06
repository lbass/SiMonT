FROM alpine:latest
RUN mkdir -p /logs
ARG JAR_FILE=target/simont-*-jar-with-dependencies.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Xms64m","-Xmx64m","-cp", "./app.jar", "com.lbass.simont.SimontApplication"]