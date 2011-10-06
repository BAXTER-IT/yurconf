<%@page import="com.baxter.config.model.log4j.Logger"%>
<%@page import="com.baxter.config.model.log4j.Appender"%>
<html>
<%@page import="com.baxter.config.servlet.Component"%>
<%@page import="com.baxter.config.model.log4j.Configuration"%>
<head>
<title>Logging - Baxter Config</title>
<style>
<!--
table.root {
	border: none;
	width: 100%;
	border-spacing: 1px;
}

table.root th {
	background-color: #B0B0B0;
	padding: 5px;
}

table.root td {
	vertical-align: top;
}

tr.odd td {
	padding: 3px;
	background-color: #AFAFAF;
}

tr.even td {
	padding: 3px;
	background-color: #CFCFCF;
}
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
    <table class="root">
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
              boolean odd = false;
            		  for (Appender appender : conf.getTargetAppenders())
            		  {
            %>
            <tr class="<%=(odd = !odd) ? "even" : "odd"%>">
              <td><%=appender.getName()%></td>
              <td>File name</td>
              <td>Pattern here</td>
            </tr>
            <%
              }
            %>
          </table>
        </td>
        <td><table>
            <%
              odd = false;
            		  for (Logger logger : conf.getAllLoggers())
            		  {
            %>
            <tr class="<%=(odd = !odd) ? "even" : "odd"%>">
              <td><%=logger.getName()%></td>
              <td><%=logger.getLevel().getValue()%></td>
              <td><%=logger.isAdditivity()%></td>
              <td>Appenders list here</td>
            </tr>
            <%
              }
            %>
          </table>
        </td>
      </tr>
      <%
        }
        }
      %>
      <tfoot>
        <tr>
          <th colspan="2"><input type="submit" name="action" value="Save all" /></th>
        </tr>
      </tfoot>
    </table>
  </form>
</body>
</html>
