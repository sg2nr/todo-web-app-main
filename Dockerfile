FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/todo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV TZ="Europe/Rome"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN date
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]