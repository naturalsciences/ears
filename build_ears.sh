base=$(pwd)
cd $base/ears3-ontology-base
mvn clean install

cp $base/ears3-ontology-base/target/uber-ears3-ontology-base-0.9-SNAPSHOT.jar $base/moduleBusinessLayerWS/release/modules/ext/uber-ears3-ontology-base-0.9-SNAPSHOT.jar

mvn install:install-file -Dfile=$base/moduleBusinessLayerWS/release/modules/ext/uber-ears3-ontology-base-1.0-SNAPSHOT.jar -DgroupId=be.naturalsciences.bmdc.ears.ontology -DartifactId=ears-ontology-base-uber -Dversion=1.0 -Dpackaging=jar 

mvn install:install-file -Dfile=$base/moduleBusinessLayerWS/release/modules/ext/uber-ears3-ontology-base-1.0-SNAPSHOT.jar -DgroupId=be.naturalsciences.bmdc.ears.ontology -DartifactId=ears-ontology-base -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=$base/moduleBusinessLayerWS/release/modules/ext/uber-ears3-ontology-base-1.0-SNAPSHOT.jar -DgroupId=be.naturalsciences.bmdc.ears -DartifactId=ears2-base-objects -Dversion=1.0 -Dpackaging=jar
mvn clean install
