FROM openjdk:17

WORKDIR /usr/share/app

COPY target/*.jar app.jar

EXPOSE 8090

CMD [ "java", "-jar", "app.jar" ]