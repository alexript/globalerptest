#!/bin/bash
export JDK_HOME=~/Java/openjdk-11.0.2
export PATH=$JDK_HOME/bin:$PATH

javac src/main/java/TreeRender.java -d target/classes
