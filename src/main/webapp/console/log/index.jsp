<%@page import="com.baxter.config.model.log4j.Logger"%>
<%@page import="com.baxter.config.model.log4j.Appender"%>
<html>
<%@page import="com.baxter.config.servlet.Component"%>
<%@page import="com.baxter.config.model.log4j.Configuration"%>
<head>
<title>Logging - Baxter Config</title>
<style>
<!--
//
-->
</style>
</head>
<body>
  <h1>Logging - Baxter Config</h1>
  <jsp:include page="../inc-menu.jsp" />
  <jsp:include page="../inc-save.jsp" />
  <hr />
  <jsp:useBean id="logs" class="com.baxter.config.bean.LogsProvider" scope="session" />
  <jsp:include page="../inc-messages.jsp" />
  <form method="post">
    <table>
      <%
        for (Component comp : Component.values())
        {
      		final Configuration conf = logs.get(comp);
      		if (conf != null)
      		{
      %>
      <tr>
        <th colspan="2"><%=comp%></th>
      </tr>
      <tr>
        <th>Appenders</th>
        <th>Levels</th>
      </tr>
      <tr>
        <td><table>
            <%
              for (Appender appender : conf.getTargetAppenders())
            		  {
            %>
            <tr>
              <td><%=appender.getName()%></td>
              <td>Pattern here</td>
            </tr>
            <%
              }
            %>
          </table></td>
        <td><table>
            <%
              for (Logger logger : conf.getAllLoggers())
            		  {
            %>
            <tr>
              <td><%=logger.getName()%></td>
              <td><%=logger.getLevel().getValue()%></td>
              <td><%=logger.isAdditivity()%></td>
              <td>Appenders list here</td>
            </tr>
            <%
              }
            %>
          </table></td>
      </tr>
      <%
        }
        }
      %>
    </table>
  </form>
</body>
</html>
