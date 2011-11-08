<jsp:useBean id="msg" class="com.baxter.config.bean.Messages" scope="request" />
<%
  if ( ! msg.isEmpty() ) {
  %>
<p>Some errors occurred:
<ul class="error" style="color:red;">
  <%
  for ( String m : msg.getItems() ) {
%>
  <li><%= m %></li>
  <%  
  }
  %>
</ul>
</p>
<hr />
<%
  }
%>