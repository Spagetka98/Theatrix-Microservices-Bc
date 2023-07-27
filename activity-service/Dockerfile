FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} activity-service.jar
ENTRYPOINT ["java","-jar","/activity-service.jar"]
EXPOSE 9002