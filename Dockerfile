FROM siengedev/jdk8-with-dockerize:0.1

ADD build/libs/*.jar /build/app.jar
ARG DOptions=""

RUN echo "java -Djava.security.egd=file:/dev/./urandom -Xms128m -Xmx128m ${DOptions} \$1 -jar /build/app.jar" >> /build/entrypoint.sh
RUN chmod 777 /build/entrypoint.sh
CMD ["sh", "-c", "/build/entrypoint.sh"]
