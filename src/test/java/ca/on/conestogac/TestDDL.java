package ca.on.conestogac;

import org.junit.Test;

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
        	System.out.println(ResultSetValue.toJsonString(oRs));
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
        	System.out.println(ResultSetValue.toJsonString(oRs));
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

}
