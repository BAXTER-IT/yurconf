@SETLOCAL
@ECHO OFF

SET YC_HOME=%~dp0..
ECHO ConfigServer Home: %YC_HOME%

rem SET JAVA_OPTS=%JAVA_OPTS% "-Xrunjdwp:transport=dt_socket,address=8822,server=y,suspend=y"
SET JAVA_OPTS=%JAVA_OPTS% "-Dyurconf.home=%YC_HOME%"
SET JAVA_OPTS=%JAVA_OPTS% "-Dlogback.configurationFile=%YC_HOME%\etc\configuration-server-log-logback.xml"
SET JAVA_OPTS=%JAVA_OPTS% "-Dcom.baxter.config.Processors=%YC_HOME%\processors"
SET JAVA_OPTS=%JAVA_OPTS% "-Dcom.baxter.config.variants=SAN"
If "%1z" == "z" (
	echo The default repository will be used.
	SET JAVA_OPTS=%JAVA_OPTS% "-Dcom.baxter.config.Repository=%YC_HOME%\repository"
) ELSE (
	echo The repository path is overridden.
	SET JAVA_OPTS=%JAVA_OPTS% "-Dcom.baxter.config.Repository=%1"
)

ECHO Java Home: %JAVA_HOME%
IF "" == "%JAVA_HOME%" (
    SET JAVACMD=java
) ELSE (
    SET JAVACMD="%JAVA_HOME%\bin\java.exe"
)

ECHO Java options: %JAVA_OPTS%

echo maci
echo %JAVACMD%
echo %JAVA_OPTS%
echo %YC_HOME%


%JAVACMD% %JAVA_OPTS% -cp "%YC_HOME%\lib\*" com.baxter.config.server.Main
rem %JAVACMD% %JAVA_OPTS% -cp "%YC_HOME%\lib\*" com.baxter.config.server.Main -h 0.0.0.0 -p 4040 -l "%YC_HOME%\lib\processors" -r "%YC_HOME%\repository"
pause

@ENDLOCAL
pause
