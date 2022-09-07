rm -rf /opt/sonarqube/temp
su sonarqube
java -jar /opt/sonarqube/lib/sonar-application-9.1.0.47736.jar -Dsonar.log.console=true 
