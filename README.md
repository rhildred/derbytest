derbytest
=========

derbytest is an embedded derby database that can be hosted on openshift

features include:

- connects in the data directory so that database maintains state across commits
- migration script (using flyway) is raked on every push to openshift

An example is ca.on.conestogac.Lab4.

	package ca.on.conestogac;
	
	import java.sql.*;
	
	public class Lab4 {
		public static void main(String[] args) {
			//set these to be null so that we can finally close them
	        Connection connection = null;
	        Statement oStmt = null;
	        try{
	        	//make a stmt from my SQL
	        	connection = OpenShiftDerbySource.getConnection();
	        	oStmt = connection.createStatement();
	        	String sSQL = "SELECT * FROM PERSON";
	        	ResultSet oRs = oStmt.executeQuery(sSQL);
	        	System.out.println(ResultSetValue.toJsonString(oRs));
	            oRs.close();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally{
	        	try{
	        		if(oStmt != null) oStmt.close();
	        		if(connection != null) connection.close();
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        }
		}
	}

This is a simple select example that also makes use of a helper `ResultSetValue.toJsonString(oRs)`.