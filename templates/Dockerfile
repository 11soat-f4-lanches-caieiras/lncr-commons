FROM ##BASE_IMAGE##
WORKDIR /##MS_DOMAIN##
COPY /##MS_DOMAIN##/target/##MS_DOMAIN##*.jar ##MS_DOMAIN##.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "##MS_DOMAIN##.jar"]
