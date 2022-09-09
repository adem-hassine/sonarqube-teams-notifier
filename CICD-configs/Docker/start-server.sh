#!/bin/bash
echo "starting sonarqube server "
java -jar /opt/sonarqube/lib/sonar-application-9.1.0.47736.jar -Dsonar.log.console=true >output.log 2>&1 &
exit
