FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY . .
RUN mvn -DskipTests clean package

FROM icr.io/appcafe/open-liberty:kernel-slim-java21-openj9-ubi-minimal

COPY src/main/liberty/config/server.xml /config/server.xml
COPY src/main/liberty/config/jdbc /config/jdbc
# This script adds the requested XML snippets to enable Liberty features and grow the image to be fit-for-purpose.
# This option is available only in the 'kernel-slim' image type. The 'full' and 'beta' tags already include all features.
RUN features.sh

COPY --from=build /workspace/target/SoccerSystem-app.war /config/apps/SoccerSystem-app.war

# This script adds the requested server configuration, applies any interim fixes, and populates caches to optimize the runtime.
RUN configure.sh
