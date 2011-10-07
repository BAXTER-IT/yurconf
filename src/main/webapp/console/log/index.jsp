<%@page import="com.baxter.config.model.log4j.Level"%>
<html>
<%@page import="com.baxter.config.model.log4j.AbstractLogger"%>
<%@page import="com.baxter.config.model.log4j.Logger"%>
<%@page import="com.baxter.config.model.log4j.Appender"%>
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
  <jsp:useBean id="msg" class="com.baxter.config.bean.Messages" scope="request" />
  <jsp:useBean id="logs" class="com.baxter.config.bean.LogsProvider" scope="session" />
  <%
    final String compParam = request.getParameter("component");
    final Component comp = (compParam != null && !compParam.isEmpty() ? Component.valueOf(compParam) : null);
    try
    {
  		if ("Apply".equals(request.getParameter("action")))
  		{
  		  if (comp != null)
  		  {
  			final Configuration conf = logs.get(comp);
  			for (Appender appender : conf.getTargetAppenders())
  			{
  			  final String output = request.getParameter("appender:" + appender.getName() + ":File");
  			  final String pattern = request.getParameter("appender:" + appender.getName() + ":Pattern");
  			  appender.setOutput(output);
  			  appender.getLayout().setPattern(pattern);
  			}
  			for (AbstractLogger logger : conf.getAllLoggers())
  			{
  			  final String level = request.getParameter("logger:" + logger.getName() + ":Level");
  			  final boolean additivity = "true".equals(request.getParameter("logger:" + logger.getName() + ":Additivity"));
  			  logger.setLevelValue(level);
  			  logger.setAdditivity(additivity);
  			}
  		  }
  		}
    }
    catch (Exception e)
    {
  		msg.add("Failed to perform requested action, see previous errors");
  		e.printStackTrace();
    }
  %>
  <jsp:include page="../inc-messages.jsp" />
  <form method="get">
    <table>
      <tr>
        <td>Choose component:</td>
        <%
          for (Component component : Component.values())
          {
        		if (comp != component)
        		{
        %>
        <td><input name="component" value="<%=component%>" type="submit" /></td>
        <%
                }
	      }
        %>
      </tr>
    </table>
  </form>
  <%
    if (comp != null)
    {
  %>
  <form method="post">
    <input type="hidden" name="component" value="<%=comp%>" />
    <h2>Logging for <%=comp%></h2>
    <%
      final Configuration conf = logs.get(comp);
      if (conf != null) {
    %>
    <h3>Appenders</h3>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Output</th>
          <th>Layout Pattern</th>
        </tr>
      </thead>
      <tbody>
        <%
          boolean odd = false;
          for (Appender appender : conf.getTargetAppenders()) {
        %>
        <tr class="<%=(odd = !odd) ? "odd" : "even"%>">
          <td><%=appender.getName()%></td>
          <%
            if (appender.isFileAppender()) {
          %>
          <td><input name="appender:<%=appender.getName()%>:File" type="text" size="40"
            value="<%=appender.getOutput()%>" /></td>
          <%
            } else {
          %>
          <td><%=appender.getOutput()%></td>
          <%
            }
          %>
          <td><input name="appender:<%=appender.getName()%>:Pattern" type="text" size="40"
            value="<%=appender.getLayout().getPattern()%>" /></td>
        </tr>
        <%
          }
        %>
      </tbody>
    </table>
    <h3>Loggers</h3>
    <table>
      <thead>
        <tr>
          <th>Logger</th>
          <th>Level</th>
          <th>Add.</th>
          <th>Effective appenders</th>
        </tr>
      </thead>
      <tbody>
        <%
        odd = false;
        for (AbstractLogger logger : conf.getAllLoggers()) {
        %>
        <tr class="<%=(odd = !odd) ? "odd" : "even"%>">
          <td><%=logger.getName()%></td>
          <td><select name="logger:<%=logger.getName()%>:Level" size="1">
              <%
                for (String level : Level.getValues()) {
              %>
              <option value="<%=level%>"<% if (level.equals(logger.getLevelValue())) {%> selected <%}%>><%=level%></option>
              <%
                }
              %>
          </select>
          </td>
          <td>
            <%
                if (!logger.isAdditivityIgnored()) {
            %> 
            <input type="checkbox" name="logger:<%=logger.getName()%>:Additivity" value="true"
            <%    if (logger.isAdditivity()) {%> 
                     checked 
            <%    } %> /> 
            <%  } else { %>
            &nbsp;
            <%  } %>
          </td>
          <td>Appenders list here</td>
        </tr>
        <%
          }
        %>
      </tbody>
    </table>
    <%
      }
    %>
    <p>
      <input type="submit" name="action" value="Apply" /> you changes.
    </p>
  </form>
  <%
    }
  %>
</body>
</html>
