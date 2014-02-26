package ca.on.conestogac;

import org.junit.Test;
import org.json.simple.*;

import javax.lang.model.*;

import static org.junit.Assert.*;

import java.sql.*;


public class TestDDL {
	
	String sNewInspectionObject ="{\"name\":\"bobsled\", \"archetypeAttributes\":[{\"name\":\"name\", \"SQLType\":\"varchar(45)\", \"formType\":\"text\"}, {\"name\":\"dateOfManufacture\", \"SQLType\":\"date\", \"formType\":\"date\"}]}";
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
	@Test
	public void testVarchar()
	{
		assertTrue("VARCHAR(45)".matches("VARCHAR[(][0-9]{1,5}[)]"));
	}
	String sNewBobsled ="{\"archetype\":\"bobsled\", \"name\":\"cool running\", \"dateOfManufacture\":\"1997-01-15\"}";
	@Test
	public void testSNewBobSled()
	{
		JSONObject oInput = (JSONObject)JSONValue.parse(sNewBobsled);
		assertTrue(((String)oInput.get("archetype")).equals("bobsled"));
		
	}
	@Test
	public void testBobSledCreation()
	{
		InspectionObject oBobsled = new InspectionObject(sNewBobsled);
		try {
			oBobsled.save();
			Connection insConnection = OpenShiftDerbySource.getConnection();
        	Statement oStmt = insConnection.createStatement();
        	// we would send this string back to the client
        	ResultSet oRs = oStmt.executeQuery("SELECT * FROM bobsled");
        	System.out.println(ResultSetValue.toJsonString(oRs));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);
	}
	@Test
	public void testBobSledUpdation()
	{
		try {
			InspectionObject oBobsled = new InspectionObject(sNewBobsled);
			oBobsled.save();
			oBobsled.set("name", "cool stopping");
			oBobsled.save();
			Connection insConnection = OpenShiftDerbySource.getConnection();
        	Statement oStmt = insConnection.createStatement();
        	// we would send this string back to the client
        	ResultSet oRs = oStmt.executeQuery("SELECT * FROM bobsled");
        	System.out.println(ResultSetValue.toJsonString(oRs));
			
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);
	}
	@Test
	public void testBobSledDeletion()
	{
		try {
			InspectionObject oBobsled = new InspectionObject(sNewBobsled);
			oBobsled.save();
			oBobsled.delete();
			Connection insConnection = OpenShiftDerbySource.getConnection();
        	Statement oStmt = insConnection.createStatement();
        	// we would send this string back to the client
        	ResultSet oRs = oStmt.executeQuery("SELECT * FROM bobsled");
        	System.out.println(ResultSetValue.toJsonString(oRs));
			
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);
	}
}
