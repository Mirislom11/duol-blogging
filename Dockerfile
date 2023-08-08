FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Tashkent
EXPOSE 8081
ENTRYPOINT ["java",  "-Dfile.encoding=UTF-8", "-Xmx2g", "-Xms2g", "-jar", "app.jar"]

