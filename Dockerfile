FROM openjdk:8-alpine

COPY target/uberjar/berita.jar /berita/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/berita/app.jar"]
