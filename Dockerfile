FROM azul/zulu-openjdk-alpine:11

COPY ./backend/target/scala-2.13/app.jar /app/app.jar
WORKDIR /app

EXPOSE 8080

ENTRYPOINT [ "java", "-Dport=8080", "-DisProd=true", "-jar", "app.jar", "caskweb.Main" ]
