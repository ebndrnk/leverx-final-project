FROM gradle:jdk17-alpine AS build

WORKDIR /app


COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .


RUN gradle dependencies --no-daemon

COPY . .

RUN gradle assemble --no-daemon

FROM bellsoft/liberica-openjdk-alpine:17.0.10

COPY --from=build /app/build/libs/leverx-final-project-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "leverx-final-project-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
