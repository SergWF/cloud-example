FROM oracle/serverjre:8
MAINTAINER Sergii Bilenogov
EXPOSE 9001
RUN mkdir -p /app/
ADD target/service-config-*-SNAPSHOT.jar /app/service-config.jar
ENV spring.cloud.config.server.git.uri https://github.com/SergWF/cloud-example.git/configs
ENTRYPOINT ["java", "-jar", "/app/service-config.jar"]