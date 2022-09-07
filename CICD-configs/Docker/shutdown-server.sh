#!/bin/bash 
# hard shutdown of sonarqube server 
exec kill -kill $(pgrep java) 
