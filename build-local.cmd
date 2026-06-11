@echo off
setlocal
set "ROOT=%~dp0"
set "JAVA_HOME=%ROOT%.tools\jdk-17.0.19+10"
set "PATH=%JAVA_HOME%\bin;%ROOT%.tools\apache-maven-3.9.9\bin;%PATH%"

if "%~1"=="" (
  mvn "-Dmaven.repo.local=%ROOT%.tools\.m2" package
) else (
  mvn "-Dmaven.repo.local=%ROOT%.tools\.m2" %*
)
