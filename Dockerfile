FROM --platform=linux/amd64 eclipse-temurin:21.0.2_13-jre-alpine
MAINTAINER Nghia
COPY ./build/libs/auth.jwt-0.0.1-SNAPSHOT.jar /home/auth.jwt-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","/home/auth.jwt-0.0.1-SNAPSHOT.jar"]