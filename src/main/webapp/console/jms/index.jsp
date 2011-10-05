<html>
<%@page import="com.baxter.config.model.Group" %>
<head>
<title>JMS - Baxter Config</title>
<style>
<!--
table.jmsinst {
	border: none;
	background-color: #202020;
	border-spacing: 1px;
}

table.jmsinst th,table.jmsinst tfoot td {
	background-color: #CFCFCF;
	padding: 3px;
}

table.jmsinst tr.odd td {
	padding: 3px;
	background-color: #AFAFAF;
}

table.jmsinst tr.even td {
	padding: 3px;
	background-color: #CFCFCF;
}
table.jmschannels {
    border: none;
    background-color: #202020;
    border-spacing: 1px;
}
//
-->
</style>
</head>
<body>
  <h1>JMS - Baxter Config</h1>
  <hr />
  <jsp:useBean id="props" class="com.baxter.config.model.Properties" scope="session">
    <jsp:setProperty name="props" property="loadFrom" value="default" />
  </jsp:useBean>
  <form method="post">
    <%
      if ("Delete".equals(request.getParameter("action")))
      {

      }
      if ("Add".equals(request.getParameter("action")))
      {
    		final String newHost = request.getParameter("newHost");
    		final String newPort = request.getParameter("newPort");
    		final String newRouter = request.getParameter("newRouter");
    		final String newUsername = request.getParameter("newUsername");
    		final String newPassword = request.getParameter("newPassword");
    		props.addNewJmsInstance(newHost, newPort, newRouter, newUsername, newPassword);
      }
    %>
    <table class="jmsinst">
      <thead>
        <tr>
          <th>#</th>
          <th>Host</th>
          <th>Port</th>
          <th>Router</th>
          <th>Username</th>
          <th>Password</th>
          <th>Opts</th>
        </tr>
      </thead>
      <tbody>
        <%
          for (int i = 1; i <= props.getJMSInstancesCount(); i++)
          {
        %>
        <tr class="<%=(i % 2 == 0) ? "even" : "odd"%>">
          <td><%=i%></td>
          <td><%=props.getAlias("Host" + i).getValue()%></td>
          <td><%=props.getAlias("Port" + i).getValue()%></td>
          <td><%=props.getAlias("Router" + i).getValue()%></td>
          <td><%=props.getAlias("UserName" + i).getValue()%></td>
          <td>********</td>
          <%
            if (i == props.getJMSInstancesCount())
          		{
          %>
          <td><input type="submit" value="Delete" name="action" />
          </td>
          <%
            }
          		else
          		{
          %>
          <td>&nbsp;</td>
          <%
            }
          %>
        </tr>
        <%
          }
        %>
      </tbody>
      <tfoot>
        <tr>
          <td>New</td>
          <td><input type="text" size="15" name="newHost" />
          </td>
          <td><input type="text" size="6" name="newPort" />
          </td>
          <td><input type="text" size="10" name="newRouter" />
          </td>
          <td><input type="text" size="20" name="newUsername" />
          </td>
          <td><input type="password" size="20" name="newPassword" />
          </td>
          <td><input type="submit" value="Add" name="action" />
          </td>
        </tr>
      </tfoot>
    </table>
  </form>
  <hr />
  <table class="jmschannels">
    <thead>
      <tr>
        <th>Type</th>
        <th>Name</th>
        <th>Alias</th>
        <%
          for (int i = 1; i <= props.getJMSInstancesCount(); i++)
          {
        %>
        <th>JMS <%=i%></th>
        <%
          }
        %>
      </tr>
    </thead>
    <tbody>
        <%
          for (Group channel : props.getChannels())
          {
        %>
        <tr>
        <td><%= channel.getChannelType() %></td>
        <td><%= channel.getChannelName() %></td>
        <td><%= channel.getKey() %></td>
        <%
          for (int i = 1; i <= props.getJMSInstancesCount(); i++)
          {
        %>
        <td><input type="radio" name="channel_<%= channel.getKey() %>" value="<%= i %>"
        <%
          if ( i==channel.getJMSIndex() ) {
         %>selected="true"<% 
          }
        %> 
        /></td>
        <%
          }
        %>
        </tr>
        <%
          }
        %>
    </tbody>
  </table>
</body>
</html>