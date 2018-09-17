FROM openjdk:8-jdk-alpine as builder

RUN mkdir -p /gradle

ADD  build.gradle /gradle
ADD  settings.gradle /gradle
ADD  src /gradle/src
ADD  gradlew /gradle
ADD  gradle /gradle/gradle
WORKDIR /gradle
RUN ./gradlew build

FROM adoptopenjdk/openjdk8-openj9:x86_64-alpine-jdk8u181-b13_openj9-0.9.0-slim

ENV REDIS_PORT 6379
ENV REDIS_HOST localhost
ENV EUREKA_URL localhost:8761
ENV SERVER_PORT 8080
ENV XMX 128m


RUN mkdir -p /app
COPY --from=builder /gradle/build/libs/gateway.jar /app/

CMD ["java", \
    "-Xmx${XMX}", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-Xscmx128m", "-Xscmaxaot100m", "-Xshareclasses:cacheDir=/opt/shareclasses", \
    "-jar", "gateway.jar", \
    "--spring.redis.host=${REDIS_HOST}", \
    "--spring.redis.port=${REDIS_PORT}", \
    "--server.port=${SERVER_PORT}", \
    "--eureka.client.serviceurl.defaultzone=http://${EUREKA_URL}/eureka/" \
]