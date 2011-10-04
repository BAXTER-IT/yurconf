<%
  final String configType = request.getParameter("configType");
  final String componentId = request.getParameter("componentId");
  final String redirect = "../rest/" + configType + "/" + componentId + "?version=1.0";
  response.sendRedirect( redirect );
%>