@echo off
java -XX:+UseSerialGC -XX:+UseStringDeduplication -Dfile.encoding=UTF-8 -jar Simulator.jar