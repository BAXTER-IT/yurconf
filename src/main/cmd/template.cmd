
BAXTER_CONFIG=http://localhost:8080/baxter-config/rest
BAXTER_COMP=dbserver

JAVA_OPTS=%JAVA_OPTS% -Dlog4j.configuration=%BAXTER_CONFIG%/log4j/%BAXTER_COMP%
JAVA_OPTS=%JAVA_OPTS% -Dlogs.dir=c:\pelogs
JAVA_OPTS=%JAVA_OPTS% -Dbaxter.config.url=%BAXTER_CONFIG%
JAVA_OPTS=%JAVA_OPTS% -Dbaxter.component.id=%BAXTER_COMP%
