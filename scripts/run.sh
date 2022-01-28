# shellcheck disable=SC1113 mvn spring-boot:run
#/bin/sh
mvn clean package -DskipTests && java -jar ./target/optimisation-service-1.0.0-SNAPSHOT.jar