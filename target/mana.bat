@echo off
java -version
cd/d %~dp0
java -jar -Xms256M -Xmx512M mana-1.0.0.jar