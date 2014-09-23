@setlocal
@echo off

set YC_HOME=%~dp0..
echo ConfigServer Home: %YC_HOME%

set MAIN_JAR="%YC_HOME%\lib\${project.build.finalName}.jar"
set JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.home=%YC_HOME%"
set JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.host=0.0.0.0"
set JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.port=4040"

set JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.processors=%YC_HOME%\lib\processors"
set JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.repository=%YC_HOME%\repository"

set JAVA_OPTS=%JAVA_OPTS% "-Dlogback.configurationFile=%YC_HOME%\etc\yurconf-server-log.xml"

echo Java Home: %JAVA_HOME%
if "" == "%JAVA_HOME%" (
    set JAVACMD=java
) else (
    set JAVACMD="%JAVA_HOME%\bin\java.exe"
)

echo Java options: %JAVA_OPTS%
%JAVACMD% %JAVA_OPTS% -jar %MAIN_JAR%

@endlocal
