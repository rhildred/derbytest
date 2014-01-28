<%@page contentType="application/json; charset=UTF-8"%>
<%@page import="ca.on.conestogac.*"%>
<%@page import="java.sql.*"%>
<%

try{
	Connection oConnection = OpenShiftDerbySource.getConnection();
	Statement oStmt = oConnection.createStatement();
	String sSQL = "SELECT * FROM PERSON";
	ResultSet oRs = oStmt.executeQuery(sSQL);
	out.println(ResultSetValue.toJsonString(oRs));
}
catch(Exception e)
{
	out.println(e.getStackTrace());
}


%>