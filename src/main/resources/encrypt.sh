#!/bin/bash

java -Dspring.profiles.active=prod -classpath Scaffold.jar com.narusec.app.util.PasswordEncryption $1