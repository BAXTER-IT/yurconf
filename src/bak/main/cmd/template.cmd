
echo "Configuring PE Component from the Central Configuration Store..."

set BAXTER_CONFIG=http://localhost:8080/baxter-config/rest
set BAXTER_COMP=%1

set LOG4JXML=%BAXTER_CONFIG%/log4j/%BAXTER_COMP%/log4j.xml
echo "Log4J XML: %LOG4JXML%"

set JAVA_OPTS=%JAVA_OPTS% -Dlog4j.configuration=%LOG4JXML%
set JAVA_OPTS=%JAVA_OPTS% -Dlogs.dir=c:\Baxter\pelogs
set JAVA_OPTS=%JAVA_OPTS% -Dbaxter.config.url=%BAXTER_CONFIG%
set JAVA_OPTS=%JAVA_OPTS% -Dbaxter.component.id=%BAXTER_COMP%

echo "JAVA_OPTS: %JAVA_OPTS%"
