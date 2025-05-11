#!/bin/bash
mvn clean -f pom.xml
mvn package -f pom.xml
mvn compile -f pom.xml