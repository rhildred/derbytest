package ca.on.conestogac;

import org.junit.Test;
import org.json.simple.*;
import javax.lang.model.*;



import static org.junit.Assert.*;

import java.sql.*;


public class TestDDL {
	
	@Test
	public void testDispClass()
	{
		//set these to be null so that we can finally close them
        Connection connection = null;
        Statement oStmt = null;
        try{
        	//make a stmt from my SQL
        	connection = OpenShiftDerbySource.getConnection();
        	oStmt = connection.createStatement();
        	String sSQL = "SELECT * FROM DispClass";
        	ResultSet oRs = oStmt.executeQuery(sSQL);
        	// we would send this string back to the client
        	ResultSetValue.toJsonString(oRs);
            oRs.close();
    		assertTrue(true);
        }catch(Exception e){
        	e.printStackTrace();
    		assertTrue(false);
        }finally{
        	try{
        		if(oStmt != null) oStmt.close();
        		if(connection != null) connection.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}
	@Test
	public void testDispAttribute()
	{
		//set these to be null so that we can finally close them
        Connection connection = null;
        Statement oStmt = null;
        try{
        	//make a stmt from my SQL
        	connection = OpenShiftDerbySource.getConnection();
        	oStmt = connection.createStatement();
        	String sSQL = "SELECT * FROM DispAttribute";
        	ResultSet oRs = oStmt.executeQuery(sSQL);
        	// we would send this string back to the client
        	ResultSetValue.toJsonString(oRs);
            oRs.close();
    		assertTrue(true);
        }catch(Exception e){
        	e.printStackTrace();
    		assertTrue(false);
        }finally{
        	try{
        		if(oStmt != null) oStmt.close();
        		if(connection != null) connection.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}
	String sNewInspectionObject ="{\"name\":\"bobsled\", \"inspectionObjectAttributes\":[{\"name\":\"name\", \"SQLType\":\"varchar(45)\", \"formType\":\"text\"}, {\"name\":\"dateOfManufacture\", \"SQLType\":\"date\", \"formType\":\"date\"}]}";
	@Test
	public void testJSONParsing()
	{
		JSONObject oInput = (JSONObject)JSONValue.parse(sNewInspectionObject);
		assertTrue(((String)oInput.get("name")).equals("bobsled"));
	}
	@Test
	public void testNewInspectionObject()
	{
		InspectionArchetype oTest = new InspectionArchetype(sNewInspectionObject);
		assertTrue(oTest != null);
	}
	@Test
	public void testInsertionOfDispClassAndAttributes()
	{
		try{
			InspectionArchetype oTest = new InspectionArchetype(sNewInspectionObject);
			oTest.delete();
			oTest.save();
			assertTrue(true);
		}catch(Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}
	@Test
	public void testVersion()
	{
		assertTrue(SourceVersion.isName("test me") == false);
	}
	
}
