FROM maven:3.5.4-jdk-8

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD pom.xml $PROJECT_DIR
RUN mvn dependency:resolve -B

ADD ./ $PROJECT_DIR/
RUN mvn install -DskipTests

FROM openjdk:8-jdk

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY --from=0 $PROJECT_DIR/target/spring-library.jar $PROJECT_DIR/

EXPOSE 8080

CMD ["java", "-jar", "/opt/project/spring-library.jar"]
