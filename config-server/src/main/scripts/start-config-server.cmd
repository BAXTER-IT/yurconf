setlocal
rem
rem Baxter Configuration Server start script
rem v. ${project.version}

set SCRIPT_DIR=%~dp0
set JETTY_HOME=%SCRIPT_DIR%..
set JAVA=java.exe

pushd %JETTY_HOME%

echo Starting Jetty Container...
"%JAVA%" -jar start.jar

popd

endlocal