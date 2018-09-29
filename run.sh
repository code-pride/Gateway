#!/usr/bin/env bash
# Run java application

java \
    -Xmx${XMX} -XX:+IdleTuningGcOnIdle -Xtune:virtualized -Xscmx128m -Xscmaxaot100m -Xshareclasses:cacheDir=/opt/shareclasses \
    -jar gateway.jar \
    --spring.redis.host=${REDIS_HOST} \
    --spring.redis.port=${REDIS_PORT} \
    --server.port=${SERVER_PORT} \
    --eureka.client.serviceUrl.defaultZone=http://${EUREKA_URL}/eureka/ \
/