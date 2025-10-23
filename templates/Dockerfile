FROM openjdk:21-jdk-slim
WORKDIR /##APP_DOMAIN##
COPY /##APP_DOMAIN##/target/##APP_DOMAIN##*.jar ##APP_DOMAIN##.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "##APP_DOMAIN##.jar"]
