@echo off
setlocal
set "ROOT=%~dp0"
set "JAVA_HOME=%ROOT%.tools\jdk-17.0.19+10"
set "PATH=%JAVA_HOME%\bin;%PATH%"

java "-Djava.net.preferIPv4Stack=true" -jar "%ROOT%target\membership-0.0.1-SNAPSHOT.jar" --server.address=127.0.0.1
