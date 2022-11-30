# Build & compile with JDK and Node

FROM amazoncorretto:17 AS builder

RUN yum update -y

COPY . /app/

WORKDIR /app/api/

RUN ./mvnw clean install
  


# Running container
FROM amazoncorretto:17-alpine

RUN apk update

COPY --from=builder /app/api/target/*.jar ./app.jar

EXPOSE 9000

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

ENTRYPOINT [ "java", "-jar", "./app.jar" ]

