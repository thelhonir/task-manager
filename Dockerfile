FROM adoptopenjdk/openjdk11
VOLUME /tmp
COPY target/*.jar task-manager.jar
ENTRYPOINT ["java","-jar","/task-manager.jar"]