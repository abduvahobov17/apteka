FROM docker.io/eclipse-temurin:17

WORKDIR /opt/app
COPY app.tar.gz /opt/app/

RUN bash -c "tar -xvzf app.tar.gz -C /opt/app/ && mv /opt/app/*.jar /opt/app/app.jar"
RUN rm -rf "/opt/app/app.tar.gz"

ARG SERVER_PORT
ARG DEBUG_PORT
ARG SERVER_URL

ARG SPRING_PROFILE

ARG JAVA_OPTS
ARG EXTRA_PARAMS

EXPOSE $SERVER_PORT 80
EXPOSE $DEBUG_PORT 81

ENTRYPOINT exec java $JAVA_OPTS -jar "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT" app.jar $EXTRA_PARAMS

