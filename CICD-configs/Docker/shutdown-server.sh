#!/bin/bash
echo "running like a charm"
echo "stopping sonarqube if running"
kill -kill $(pgrep java)
echo "deleting temporary files if exists"
rm -rf /opt/sonarqube/temp/
