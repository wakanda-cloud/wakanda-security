FROM openjdk:8-alpine

RUN apk add --update wget
RUN apk add --update bash && rm -rf /var/cache/apk/*

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

ADD build/libs/*.jar /build/app.jar
ARG DOptions=""

RUN echo "java -Djava.security.egd=file:/dev/./urandom -Xms128m -Xmx128m ${DOptions} \$1 -jar /build/app.jar" >> /build/entrypoint.sh
RUN chmod 777 /build/entrypoint.sh
CMD ["sh", "-c", "/build/entrypoint.sh"]
