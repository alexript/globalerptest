#!/bin/bash
export JDK_HOME=~/Java/openjdk-11.0.2
export PATH=$JDK_HOME/bin:$PATH
cd src/main/java
export INPUT_FILE_NAME=../../../input.txt
export OUTPUT_FILE_NAME=../../../output.txt
java --source 11 TreeRender.java $INPUT_FILE_NAME $OUTPUT_FILE_NAME
