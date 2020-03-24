FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/*.jar examen.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVAOPTS -Djava.security.egd=file:/dev/./urandom -jar /examen.jar"]
EXPOSE 8080
