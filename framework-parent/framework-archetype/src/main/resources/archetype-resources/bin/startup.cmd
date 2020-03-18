@echo off
if not exist "%JAVA_HOME%\bin\java.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal enabledelayedexpansion

set PROFILE="prod"
set PROFILE_INDEX=-1

set i=0
for %%a in (%*) do (
   if "%%a" == "-p" ( set /a PROFILE_INDEX=!i!+1 )
   set /a i+=1
)

set i=0
for %%a in (%*) do (
   if %PROFILE_INDEX% == !i! ( set PROFILE="%%a" )
   set /a i+=1
)

set BASE_DIR=%~dp0
set BASE_DIR="%BASE_DIR:~0,-5%"

set DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
set CUSTOM_SEARCH_LOCATIONS=%DEFAULT_SEARCH_LOCATIONS%,file:%BASE_DIR%/config/

set SERVER=${artifactId}

set "JAVA_OPT=%JAVA_OPT% -Xms512m -Xmx512m -Xmn256m -XX:+UseG1GC"

set "JAVA_OPT=%JAVA_OPT% -Dhome=%BASE_DIR%"
set "JAVA_OPT=%JAVA_OPT% -jar %BASE_DIR%\target\%SERVER%.jar"
set "JAVA_OPT=%JAVA_OPT% --spring.config.location=%CUSTOM_SEARCH_LOCATIONS%"

if %PROFILE% == "test" (
    set "JAVA_OPT=%JAVA_OPT% --spring.profiles.active=test"
) else (
    set "JAVA_OPT=%JAVA_OPT% --spring.profiles.active=prod"
)

call "%JAVA%" %JAVA_OPT% %SERVER%.%SERVER% %*
