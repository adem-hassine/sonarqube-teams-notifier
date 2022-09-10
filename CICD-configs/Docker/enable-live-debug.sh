#!/bin/sh
# this script is created in order to enable live debug on port 8000 
# in order to enable live debug on running sonarqube server in k8s we need to 
# 1- shutdown the server 
# 2- modify files .properties to enable live debug 
# 3- restart the server 
# 4- wait for connection establishment 
# 5- debug !!


#  note : still not able to restart server with command line from the sonarqube instance 
#  workaroud 
#  override sonar.properties 
#  using sonarqube api we restart the server that's all 



# close server 

properties="c29uYXIud2ViLmphdmFBZGRpdGlvbmFsT3B0cz0tYWdlbnRsaWI6amR3cD10cmFuc3BvcnQ9ZHRfc29ja2V0LHNlcnZlcj15LHN1c3BlbmQ9eSxhZGRyZXNzPSo6ODAwMApzb25hci5jZS5qYXZhQWRkaXRpb25hbE9wdHM9LWFnZW50bGliOmpkd3A9dHJhbnNwb3J0PWR0X3NvY2tldCxzZXJ2ZXI9eSxzdXNwZW5kPXksYWRkcmVzcz0qOjgwODAK"
# decode and override sonar.properties 
echo $properties | base64 -d > /opt/sonarqube/conf/sonar.properties
