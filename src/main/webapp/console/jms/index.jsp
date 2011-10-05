<html>
<%@page import="com.baxter.config.model.Group"%>
<%@page import="com.baxter.config.model.AbstractChannelGroup"%>
<%@page import="com.baxter.config.model.QueueGroup"%>
<%@page import="com.baxter.config.model.TopicGroup"%>
<head>
<title>JMS - Baxter Config</title>
<style>
<!--
table.jmsinst,table.jmschannels {
	border: none;
	background-color: #202020;
	border-spacing: 1px;
}

th,tfoot td {
	background-color: #CFCFCF;
	padding: 3px;
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
  <h1>JMS - Baxter Config</h1>
  <jsp:include page="../inc-menu.jsp" />
  <jsp:include page="../inc-save.jsp" />
  <hr />
  <jsp:useBean id="props" class="com.baxter.config.model.Properties" scope="session">
    <jsp:setProperty name="props" property="loadFrom" value="default" />
  </jsp:useBean>
  <jsp:useBean id="newJms" class="com.baxter.config.bean.NewJmsBean" scope="request" />
  <jsp:useBean id="newChannel" class="com.baxter.config.bean.NewChannelBean" scope="request" />
  <%
    if ("Delete JMS".equals(request.getParameter("action")))
    {
  		props.deleteLastJMS();
    }
    if ("Add JMS".equals(request.getParameter("action")))
    {
  %><jsp:setProperty name="newJms" property="*" />
  <%
    props.addNewJmsInstance(newJms);
    }
    if ("Add Channel".equals(request.getParameter("action")))
    {
  %><jsp:setProperty name="newChannel" property="*" />
  <%
    props.addNewChannel(newChannel);
    }
  %>
  <jsp:include page="../inc-messages.jsp" />
  <form method="post">
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
          <td><input type="submit" value="Delete JMS" name="action" />
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
          <td><input type="text" size="15" name="host" value="<jsp:getProperty name="newJms" property="host" />" />
          </td>
          <td><input type="text" size="6" name="port" value="<jsp:getProperty name="newJms" property="port" />" />
          </td>
          <td><input type="text" size="10" name="router"
            value="<jsp:getProperty name="newJms" property="router" />" />
          </td>
          <td><input type="text" size="20" name="username"
            value="<jsp:getProperty name="newJms" property="username" />" />
          </td>
          <td><input type="password" size="20" name="password"
            value="<jsp:getProperty name="newJms" property="password" />" />
          </td>
          <td><input type="submit" value="Add JMS" name="action" />
          </td>
        </tr>
      </tfoot>
    </table>
  </form>
  <hr />
  <form method="post">
    <table class="jmschannels">
      <thead>
        <tr>
          <th>Type</th>
          <th>Channel Name</th>
          <th>Alias (key)</th>
          <%
            for (int i = 1; i <= props.getJMSInstancesCount(); i++)
            {
          %>
          <th>JMS <%=i%></th>
          <%
            }
          %>
          <th>&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <%
          boolean odd = false;
          for (Group channel : props.getChannels())
          {
        %>
        <tr class="<%=(odd = !odd) ? "even" : "odd"%>">
          <td><%=channel.getChannelType()%></td>
          <td><%=channel.getChannelName()%></td>
          <td><%=channel.getKey()%></td>
          <%
            for (int i = 1; i <= props.getJMSInstancesCount(); i++)
          		{
          %>
          <td><input type="radio" name="channel_<%=channel.getKey()%>" value="<%=i%>"
            <%if (i == channel.getJMSIndex())
		  {%> checked="true" <%}%> /></td>
          <%
            }
          %>
          <td>Current JMS: <%=channel.getJMSIndex()%></td>
        </tr>
        <%
          }
        %>
      </tbody>
      <tfoot>
        <tr>
          <td><select name="type" size="1">
              <option value="T" <%if ("T".equals(newChannel.getType()))
	  {%> selected <%}%>>Topic</option>
              <option value="Q" <%if ("Q".equals(newChannel.getType()))
	  {%> selected <%}%>>Queue</option>
          </select></td>
          <td><input type="text" name="name" size="20"
            value="<jsp:getProperty name="newChannel" property="name" />" /></td>
          <td><input type="text" name="alias" size="20"
            value="<jsp:getProperty name="newChannel" property="alias" />" /></td>
          <%
            for (int i = 1; i <= props.getJMSInstancesCount(); i++)
            {
          %>
          <td><input type="radio" name="jmsIndex" value="<%=i%>" <%if (i == newChannel.getJmsIndex())
		{%> checked
            <%}%> /></td>
          <%
            }
          %>
          <td>
            <%
              if (props.getJMSInstancesCount() != 0)
              {
            %> <input type="submit" value="Add Channel" name="action" /> <%
   }
   else
   {
 %> You cannot define new channel. Please first create JMS Instances. <%
   }
 %>
          </td>
        </tr>
      </tfoot>
    </table>
  </form>
</body>
</html>