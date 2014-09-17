@setlocal
@echo off

set CFG_HOME=%~dp0..
echo ConfigServer Home: %CFG_HOME%

set JETTY_JAR="%CFG_HOME%\lib\start-6.1.10.jar"
set JAVA_OPTS=%JAVA_OPTS% "-Djetty.home=%CFG_HOME%" 
set JAVA_OPTS=%JAVA_OPTS% "-Djetty.host=0.0.0.0" 
set JAVA_OPTS=%JAVA_OPTS% "-Djetty.port=4040" 

set JAVA_OPTS=%JAVA_OPTS% "-DSTART=%CFG_HOME%\etc\jetty-standalone.conf"
set JAVA_OPTS=%JAVA_OPTS% "-Djetty.contextsDir=%CFG_HOME%\etc\contexts"
set JAVA_OPTS=%JAVA_OPTS% "-Dprocessors.dir=%CFG_HOME%\processors"
set JAVA_OPTS=%JAVA_OPTS% "-Dconfig-server.war=%CFG_HOME%\lib\baxter-config-server-1.4.0.4.war"
set JAVA_OPTS=%JAVA_OPTS% "-Dconfig-server-web.xml=%CFG_HOME%\etc\config-server-web.xml"
set JAVA_OPTS=%JAVA_OPTS% "-Dcom.baxter.config.Repository=%CFG_HOME%\repository"

rem In fact, only one of below is needed at the time. TODO look for a way to detect which framework is in use
set JAVA_OPTS=%JAVA_OPTS% "-Dlog4j.configuration=%CFG_HOME%\etc\configuration-server-log.xml"
set JAVA_OPTS=%JAVA_OPTS% "-Dlogback.configurationFile=%CFG_HOME%\etc\configuration-server-log.xml"

echo Java Home: %JAVA_HOME%
if "" == "%JAVA_HOME%" (
    set JAVACMD=java
) else (
    set JAVACMD="%JAVA_HOME%\bin\java.exe"
)

echo Java options: %JAVA_OPTS%
%JAVACMD% %JAVA_OPTS% -jar %JETTY_JAR% "%CFG_HOME%\etc\jetty.xml"

@endlocal
