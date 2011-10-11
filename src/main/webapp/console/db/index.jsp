<html>
<%@page import="com.baxter.config.model.properties.Group"%>
<%@page import="com.baxter.config.model.properties.AbstractChannelGroup"%>
<%@page import="com.baxter.config.model.properties.QueueGroup"%>
<%@page import="com.baxter.config.model.properties.TopicGroup"%>
<head>
<title>Database - Baxter Config</title>
<style>
<!--
//
-->
</style>
</head>
<body>
  <h1>Database - Baxter Config</h1>
  <jsp:include page="../inc-menu.jsp" />
  <jsp:include page="../inc-save.jsp" />
  <hr />
  <jsp:useBean id="props" class="com.baxter.config.model.properties.Properties" scope="session" />
  <jsp:useBean id="msg" class="com.baxter.config.bean.Messages" scope="request" />
  <jsp:useBean id="dbConnection" class="com.baxter.config.bean.DBConnectionBean" scope="request">
    <jsp:setProperty name="dbConnection" property="loadFromProperties" value="<%= props %>" />
  </jsp:useBean>
  <jsp:setProperty name="dbConnection" property="*" />
  <%
    try
    {
  		if ("Apply".equals(request.getParameter("action")))
  		{
  		  props.changeDBConnection(dbConnection, msg);
  		}
    }
    catch (Exception e)
    {
  		msg.add("Failed to perform requested action, see previous errors");
  		e.printStackTrace();
    }
  %>
  <jsp:include page="../inc-messages.jsp" />
  <form method="post">
    <table class="db">
      <thead>
        <tr>
          <th>Database Family</th>
          <th>Host</th>
          <th>Port</th>
          <th>SID</th>
          <th>PWD Path</th>
          <th>&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td><select name="dbFamily" size="1" onchange="form.submit();">
              <option value="oracle" <%if ("oracle".equals(dbConnection.getDbFamily()))
	  {%> selected <%}%>>Oracle</option>
              <option value="mssql" <%if ("mssql".equals(dbConnection.getDbFamily()))
	  {%> selected <%}%>>MS
                SQL</option>
          </select></td>
          <td><input type="text" size="20" name="dbHost"
            value="<jsp:getProperty name="dbConnection" property="dbHost" />" /></td>
          <td><input type="text" size="8" name="dbPort"
            value="<jsp:getProperty name="dbConnection" property="dbPort" />" /></td>
          <td>
            <%
              if ("oracle".equals(dbConnection.getDbFamily()))
              {
            %> <input type="text" size="16" name="dbName"
            value="<jsp:getProperty name="dbConnection" property="dbName" />" /> <%
   }
   else
   {
 %> <input type="hidden"
            name="dbName" value="<jsp:getProperty name="dbConnection" property="dbName" />" /> <%
   }
 %> - N/A -
          </td>
          <td><input type="text" size="30" name="dbPwd"
            value="<jsp:getProperty name="dbConnection" property="dbPwd" />" /></td>
          <td><input type="submit" value="Apply" name="action" />
          </td>
        </tr>
      </tbody>
    </table>
  </form>
</body>
</html>