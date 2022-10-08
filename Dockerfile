FROM adoptopenjdk/openjdk11:slim

WORKDIR /usr/src/app

VOLUME /tmp

RUN apt-get update; apt-get install -y fontconfig libfreetype6

COPY target/reactive-kafka-*.jar reactive-kafka.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-XX:+UseG1GC","-Duser.timezone=Asia/Istanbul","-Djava.awt.headless=true","-jar","reactive-kafka.jar"]