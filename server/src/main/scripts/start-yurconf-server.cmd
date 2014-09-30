@SETLOCAL
@ECHO OFF

SET YC_HOME=%~dp0..
ECHO ConfigServer Home: %YC_HOME%

SET JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.home=%YC_HOME%"
SET JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.host=0.0.0.0"
SET JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.port=4040"

SET JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.processors=%YC_HOME%\lib\processors"
SET JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.repository=%YC_HOME%\repository"

SET JAVA_OPTS=%JAVA_OPTS% "-Dlogback.configurationFile=%YC_HOME%\etc\yurconf-server-log.xml"

ECHO Java Home: %JAVA_HOME%
IF "" == "%JAVA_HOME%" (
    SET JAVACMD=java
) ELSE (
    SET JAVACMD="%JAVA_HOME%\bin\java.exe"
)

SET DEPS=${windows-classpath}

ECHO Java options: %JAVA_OPTS%
%JAVACMD% %JAVA_OPTS% -cp "%YC_HOME%\lib\*" org.yurconf.server.Main

@ENDLOCAL
