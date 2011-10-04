<html>
<head>
<title>Test - Baxter Config</title>
<style>
<!--
table.input {
	background-color: #202020;
	border: none;
}

table.input td,table.input th {
	background-color: #CFCFCF;
	padding: 5px;
}
//
-->
</style>
</head>
<body>
  <h1>Test - Baxter Config</h1>
  <form action="go.jsp" method="get" target="_blank">
    <table class="input">
      <tr>
        <th>Configuration type</th>
        <td><select name="configType" size="1">
            <option value="properties">Properties</option>
            <option value="log4j">Log4J</option>
            <option value="logback">Logback</option>
            <option value="custom">Custom</option>
        </select>
        </td>
      </tr>
      <tr>
        <th>Component ID</th>
        <td><input type="text" size="30" name="componentId" />
        </td>
      </tr>
      <tr>
        <td colspan="2"><input type="submit" value="Get Configuration" /></td>
      </tr>
    </table>
  </form>
</body>
</html>