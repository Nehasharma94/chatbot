# Use Java 11
FROM openjdk:11-jdk

#COPY JAR FILE
COPY target/chatbotEntit-0.0.1-SNAPSHOT.war chatbotEntit-0.0.1-SNAPSHOT.war

ENTRYPOINT ["java","-jar","/chatbotEntit-0.0.1-SNAPSHOT.war"]



