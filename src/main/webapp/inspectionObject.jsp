<%@page contentType="application/json; charset=UTF-8"%>
<%@page import="ca.on.conestogac.*"%>
<%@page import="java.sql.*"%>
<%@page import="javax.lang.model.*"%>
<%
	try {
		if (request.getMethod().equalsIgnoreCase("get")) {
			String sArchetype = request.getParameter("archetype");
			if (!SourceVersion.isName(sArchetype))
				throw new Exception("name " + sArchetype
						+ " is not a valid java identifier");

			Connection oConnection = OpenShiftDerbySource
					.getConnection();
			Statement oStmt = oConnection.createStatement();
			String sSQL = "SELECT * FROM " + sArchetype;
			ResultSet oRs = oStmt.executeQuery(sSQL);
			out.println(ResultSetValue.toJsonString(oRs));
		} else if (request.getMethod().equalsIgnoreCase("post")) {
			String sBody = ResultSetValue.toJsonString(request
					.getInputStream());
			InspectionObject oInspection = new InspectionObject(sBody);
			oInspection.save();
			//System.out.println(body);
		} else if (request.getMethod().equalsIgnoreCase("delete")) {
			String sBody = ResultSetValue.toJsonString(request
					.getInputStream());
			InspectionObject oInspection = new InspectionObject(sBody);
			oInspection.delete();
			//System.out.println(body);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>