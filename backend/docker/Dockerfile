FROM maven:3.9.9-ibm-semeru-17-focal AS BUILDER
WORKDIR /opt/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src
RUN mvn clean install -o -DskipTests

FROM ibm-semeru-runtimes:open-17-jre-jammy
WORKDIR /var/webapp/imagehosting
COPY --from=BUILDER /opt/app/target/*.jar app.jar
RUN mkdir -p /var/testing && cp ./* /var/testing

# ENTRYPOINT ["java", "-jar", "/var/webapp/imagehosting/app.jar"]
# java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:55099 -jar app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:55099", "-jar", "/var/webapp/imagehosting/app.jar"]