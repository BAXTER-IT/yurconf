<html>
<head>
<title>JMS - Baxter Config</title>
</head>
<body>
  <h1>JMS - Baxter Config</h1>
  <jsp:useBean id="props" class="com.baxter.config.model.Properties" scope="session">
    <jsp:setProperty name="props" property="loadFrom" value="latest" />
  </jsp:useBean>
  <table>
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
      <tr>
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
        <td><input type="submit" value="Delete" />
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
        <td><input type="submit" value="Add" />
        </td>
      </tr>
    </tfoot>
  </table>
</body>
</html>