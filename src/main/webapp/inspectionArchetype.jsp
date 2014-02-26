<%@page contentType="application/json; charset=UTF-8"%>
<%@page import="ca.on.conestogac.*"%>
<%@page import="java.sql.*"%>
<%@page import="javax.lang.model.*"%>
<%
	try {
		if (request.getMethod().equalsIgnoreCase("get")) {
			String sArchetype = request.getParameter("archetype");
			String sSQL = "SELECT * FROM DispClass";
			if (sArchetype != null && !SourceVersion.isName(sArchetype))
				throw new Exception("name " + sArchetype
						+ " is not a valid java identifier");
			else if(sArchetype != null){
				sSQL = "SELECT * FROM DispAttribute WHERE idDispClass in (SELECT idDispClass FROM DispClass WHERE name = '"+ sArchetype + "')";
			}
			Connection oConnection = OpenShiftDerbySource
					.getConnection();
			Statement oStmt = oConnection.createStatement();
			ResultSet oRs = oStmt.executeQuery(sSQL);
			out.println(ResultSetValue.toJsonString(oRs));
		} else if (request.getMethod().equalsIgnoreCase("post")) {
			String sBody = ResultSetValue.toJsonString(request
					.getInputStream());
			InspectionArchetype oArchetype = new InspectionArchetype(sBody);
			oArchetype.save();
			out.println("{\"result\":\"success\"}");
			//System.out.println(body);
		} else if (request.getMethod().equalsIgnoreCase("delete")) {
			String sBody = ResultSetValue.toJsonString(request
					.getInputStream());
			InspectionArchetype oArchetype = new InspectionArchetype(sBody);
			oArchetype.delete();
			out.println("{\"result\":\"success\"}");
			//System.out.println(body);
		}
	} catch (Exception e) {
		e.printStackTrace();
		out.println(e.toString());
	}
%>