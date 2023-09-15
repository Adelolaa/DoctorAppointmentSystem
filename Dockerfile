
FROM openjdk:17-jdk-alpine
ARG JAR-FILE=build/*.jar
COPY ./build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar","/app.jar"]