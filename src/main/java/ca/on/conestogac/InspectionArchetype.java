package ca.on.conestogac;

import org.json.simple.*;
import java.sql.*;
import java.util.*;
import javax.lang.model.*;

public class InspectionArchetype {
	JSONObject oInput = null;
	public InspectionArchetype(String sInput)
	{
		oInput = (JSONObject)JSONValue.parse(sInput);
	}
	public void delete() throws Exception
	{
		Connection delConnection = null;
		PreparedStatement oStmt = null;
		ResultSet rs = null;
		try{
			//get idDispClass
			delConnection = OpenShiftDerbySource.getConnection();
			String sName = (String) oInput.get("name");
			oStmt = delConnection.prepareStatement("SELECT idDispClass FROM DispClass WHERE name = ?");
			oStmt.setString(1, sName);
			rs = oStmt.executeQuery();
			rs.next();
			long idDispClass = rs.getLong(1);
			rs.close();
			oStmt.close();
			// now we delete from dispAtributes
			delConnection.setAutoCommit(false);
			oStmt = delConnection.prepareStatement("DELETE FROM DispAttribute WHERE idDispClass = ?");
			oStmt.setLong(1, idDispClass);
			// could conceivably have a DispClass with no dispattributes
            oStmt.executeUpdate();
            oStmt.close();
            //now we delete from dispclass
            oStmt = delConnection.prepareStatement("DELETE FROM DispClass WHERE idDispClass = ?");
			oStmt.setLong(1, idDispClass);
            int affectedRows = oStmt.executeUpdate();
            if (affectedRows == 0) {
            	// we would expect to delete at least 1 here
                throw new SQLException("Deleting DispClass failed, no rows affected.");
            }
            //now we drop table ... there should be no way to get here without having a valid java identifier 
    		if(!SourceVersion.isName(sName))
    			throw new Exception("name " + sName + " is not a valid java identifier");
            delConnection.prepareStatement("DROP TABLE " + sName).executeUpdate();
			delConnection.commit();
		}catch(Exception e){
			//fail silently because we want to keep on creating a new one in the test
			delConnection.rollback();
		}finally{
			if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
            if (oStmt != null) try { oStmt.close(); } catch (SQLException logOrIgnore) {}        	
            if (delConnection != null) try { delConnection.close(); } catch (SQLException logOrIgnore) {}			
		}
	}
	Connection connection = null;
	public void save() throws Exception
	{
		try{
			connection = OpenShiftDerbySource.getConnection();
			connection.setAutoCommit(false);
			createSQLTable();
			insertDispClassAndAttributes();
			connection.commit();
		}catch(Exception e){
			connection.rollback();
			throw(e);
		}finally{
            if (connection != null) try { connection.close(); } catch (SQLException logOrIgnore) {}			
		}
	}
	private void createSQLTable() throws Exception
	{
		String sSQL = "CREATE TABLE ", sName = (String) oInput.get("name");
		if(!SourceVersion.isName(sName))
			throw new Exception("name " + sName + " is not a valid java identifier");
		sSQL += sName + "(\nid" + sName + " INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),";
		List<Map> aAttributes = (List<Map>)oInput.get("archetypeAttributes");
        for(Map oAttribute : aAttributes){
        	String sAttributeName = (String)oAttribute.get("name");
        	if(!SourceVersion.isName(sAttributeName))
    			throw new Exception("name " + sAttributeName + " is not a valid java identifier");
        	String sSQLType = ((String)oAttribute.get("SQLType")).toUpperCase();
        	if(!sSQLType.matches("INT") &&
        			!sSQLType.matches("DATE") &&
        			!sSQLType.matches("VARCHAR[(][0-9]{1,5}[)]"))
        			throw new Exception("sqlType " + sSQLType + " is not a valid SQLType");
        	sSQL += "\n" + sAttributeName + " " + sSQLType + ",";
        }
        sSQL += "\nCONSTRAINT " + sName + "_primary_key PRIMARY KEY (id" + sName + "))";
        connection.createStatement().execute(sSQL);
	}
	private void insertDispClassAndAttributes() throws Exception
	{
        PreparedStatement oStmt = null, oStmtAttribute = null;
        ResultSet generatedKeys = null;
        try{
        	//make a stmt from my SQL
        	oStmt = connection.prepareStatement("INSERT INTO DispClass(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
        	oStmt.setString(1, (String) oInput.get("name"));
            int affectedRows = oStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating DispClass failed, no rows affected.");
            }
            Long nID = 0L;
            generatedKeys = oStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                nID = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating DispClass failed, no generated key obtained.");
            }
			List<Map> aAttributes = (List<Map>)oInput.get("archetypeAttributes");
			oStmtAttribute = connection.prepareStatement("INSERT INTO DispAttribute(idDispClass, name, SQLType, formType) VALUES(?, ?, ?, ?)");
            for(Map oAttribute : aAttributes){
            	oStmtAttribute.setLong(1, nID);
            	oStmtAttribute.setString(2, (String)oAttribute.get("name"));
            	oStmtAttribute.setString(3, (String)oAttribute.get("SQLType"));
            	oStmtAttribute.setString(4, (String)oAttribute.get("formType"));
            	affectedRows = oStmtAttribute.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating DispAttribute failed, no rows affected.");
                }
            }
        }finally{
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException logOrIgnore) {}
            if (oStmt != null) try { oStmt.close(); } catch (SQLException logOrIgnore) {}        	
            if (oStmtAttribute != null) try { oStmtAttribute.close(); } catch (SQLException logOrIgnore) {}        	
        }

	}
	
}
