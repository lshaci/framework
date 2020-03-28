@echo off

if not exist "%JAVA_HOME%\bin\jps.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1

setlocal

set "PATH=%JAVA_HOME%\bin;%PATH%"
set SERVER=${artifactId}

echo killing %SERVER%.%SERVER% server

for /f "tokens=1" %%i in ('jps -m ^| find "%SERVER%.%SERVER%"') do ( taskkill /F /PID %%i )

echo Done!
