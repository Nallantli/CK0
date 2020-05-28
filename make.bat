@echo off
javac -cp . main/Main.java -encoding utf8 -d bin
cd bin
jar cvfe ../Simulator.jar main.Main .
cd ..