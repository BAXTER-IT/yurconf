<jsp:useBean id="props" class="com.baxter.config.model.Properties" scope="session">
  <jsp:setProperty name="props" property="loadFrom" value="default" />
</jsp:useBean>
<jsp:useBean id="storeManager" class="com.baxter.config.servlet.StoreManager" scope="application" />
<jsp:useBean id="persister" class="com.baxter.config.bean.PropertiesPersister" scope="page">
  <jsp:setProperty name="persister" property="storeManager" value="<%= storeManager %>" />
</jsp:useBean>
<jsp:useBean id="msg" class="com.baxter.config.bean.Messages" scope="request" />
<%
  if ( "Save".equals(request.getParameter("action") ) ) {
	%><jsp:setProperty name="persister" property="*" />
<%
    persister.save( session, msg );
  }
if ( "Load".equals(request.getParameter("action") ) ) {
  %><jsp:setProperty name="persister" property="*" />
<%
  persister.load( session, msg );
}
%>
<style>
<!--
table.save {
	width: 100%;
	border: none;
}
//
-->
</style>
<form method="post">
  <table class="save">
    <tr>
      <td>Please save your changes to make the configuration effective. You can also provide a tag for current
        configuration so that you can restore it at a later time.</td>
      <td>Tag:</td>
      <td><input type="text" name="tag" value="default" size="15" /></td>
      <td><input type="submit" name="action" value="Save" /></td>
    </tr>
    <tr>
      <td>You can also select one of previously stored configuration to make it default.</td>
      <td>Stored Tag:</td>
      <td><select name="storedTag" size="1">
          <%
      for ( String tag: persister.getStoredTags() ) {
     %><option value="<%= tag %>"><%= tag %></option>
          <% 
      }
    %>
      </select></td>
      <td><input type="submit" name="action" value="Load" /></td>
    </tr>
  </table>
</form>
