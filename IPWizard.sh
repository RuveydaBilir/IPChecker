#!/bin/bash
# Compile Java files
javac -d bin ConfigManager.java IP.java IPWizard.java RequestResponse.java UserPref.java
# Run the Java program
java -cp bin IPWizard