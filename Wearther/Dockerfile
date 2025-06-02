FROM openjdk:17-jdk

WORKDIR /project-dir

COPY ./target/*SNAPSHOT.jar project.jar

ENTRYPOINT [ "java", "-jar", "project.jar" ]