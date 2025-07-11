#build
FROM maven:3.9.10-amazoncorretto-17-al2023 AS build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

#run
FROM amazoncorretto:17.0.15
WORKDIR /app

COPY --from=build ./build/target/*.jar ./easymenuapi.jar

EXPOSE 8080

ENV DBPROD_URL=''
ENV DBPROD_USERNAME=''
ENV DBPROD_PASSWORD=''


ENV SPRING_PROFILES_ACTIVE='prod'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar easymenuapi.jar