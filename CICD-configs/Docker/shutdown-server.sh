#!/bin/bash
echo "running like a charm"
echo "stopping sonarqube if running"
kill -kill $(pgrep java)
echo "cleaning sonar.properties if live debug is enabled"
> /opt/sonarqube/conf/sonar.properties
echo "deleting temporary files if exists"
rm -rf /opt/sonarqube/temp/
echo "deleting old sonarqube installed plugins"
rm -rf /opt/sonarqube/extensions/*.jar
echo "Done !  all conditions are evaluated ! sonarqube server is properly stopped "
