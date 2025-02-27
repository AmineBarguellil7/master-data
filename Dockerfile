FROM openjdk:21
EXPOSE 8081
ADD target/master-data-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

