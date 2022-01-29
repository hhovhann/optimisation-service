# shellcheck disable=SC1113
#/bin/sh
mvn clean package -DskipTests && java -jar ./target/optimisation-service-1.0.0-SNAPSHOT.jar