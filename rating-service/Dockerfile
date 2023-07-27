FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} rating-service.jar
ENTRYPOINT ["java","-jar","/rating-service.jar"]
EXPOSE 9003