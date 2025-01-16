FROM openjdk:17-jdk-slim

ENV FILE app-1.0.0-SNAPSHOT-fat.jar

ENV HOME /app

EXPOSE 8888

COPY app/target/$FILE $HOME/
COPY config/hazelcast.yaml $HOME/config/

WORKDIR $HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FILE"]
