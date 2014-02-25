package ca.on.conestogac;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.lang.model.SourceVersion;

import org.json.simple.*;

public class InspectionObject {
	JSONObject oInput = null;
	public InspectionObject(String sInput)
	{
		oInput = (JSONObject)JSONValue.parse(sInput);
	}
	public String get(String sKey)
	{
		return (String) oInput.get(sKey);
	}
	public void set(String sKey, String sValue)
	{
		oInput.put(sKey, sValue);
	}
	public void delete() throws Exception
	{
		Connection delConnection = null;
		String sName = (String) oInput.get("archetype");
		if(!SourceVersion.isName(sName))
			throw new Exception("name " + sName + " is not a valid java identifier");
		PreparedStatement oStmt = null; 
		try{
			delConnection = OpenShiftDerbySource.getConnection();
			oStmt = delConnection.prepareStatement("DELETE FROM " + sName + " WHERE id" + sName + " = ?");
			oStmt.setString(1, (String)oInput.get("id" + sName));
			int nRows = oStmt.executeUpdate();
			if(nRows == 0) throw new Exception("no rows deleted");
		}finally{
            if (delConnection != null) try { delConnection.close(); } catch (SQLException logOrIgnore) {}			
            if (oStmt != null) try { oStmt.close(); } catch (SQLException logOrIgnore) {}			
		}
	}
	Connection insConnection = null;
	List<String> aBindVars = new LinkedList<String>();
	public void save() throws Exception
	{
		PreparedStatement oStmt = null;
		ResultSet rs = null;
		try{
			//get idDispClass
			insConnection = OpenShiftDerbySource.getConnection();
			String sName = (String) oInput.get("archetype");
			oStmt = insConnection.prepareStatement("SELECT idDispClass FROM DispClass WHERE name = ?");
			oStmt.setString(1, sName);
			rs = oStmt.executeQuery();
			rs.next();
			long idDispClass = rs.getLong(1);
			oStmt.close();
			rs.close();
			//get attributes to build sql
			oStmt = insConnection.prepareStatement("SELECT name FROM DispAttribute where idDispClass = ?");
			oStmt.setLong(1, idDispClass);
			rs = oStmt.executeQuery();
			String sSQL = null;
			boolean bInsert = false;
			aBindVars.clear();
			if(oInput.get("id" + oInput.get("archetype")) == null){
				sSQL = doInsert(rs);
				bInsert = true;
			}
			else{
				sSQL = doUpdate(rs);
			}
			oStmt.close();
			oStmt = insConnection.prepareStatement(sSQL, Statement.RETURN_GENERATED_KEYS);
			int nColumn = 1;
			for(String sValue: aBindVars){
				oStmt.setString(nColumn++, sValue);
			}
			int nRowsAffected = oStmt.executeUpdate();
			if(nRowsAffected == 0)throw new Exception("0 rows updated by insert");
			if(bInsert)
			{
	            rs.close();
	            rs = oStmt.getGeneratedKeys();
	            if (rs.next()) {
	                set("id" + sName, rs.getString(1));
	            } else {
	                throw new SQLException("Creating " + sName + " failed, no generated key obtained.");
	            }

			}
		}finally{
            if (insConnection != null) try { insConnection.close(); } catch (SQLException logOrIgnore) {}			
		}
	}
	private String doUpdate(ResultSet rs) throws Exception {
		int nColNo = 0;
		String sName = (String) oInput.get("archetype");
		String sSQL = "UPDATE " + sName + " SET ";
		while(rs.next()){
			String sColName = rs.getString(1);
			if(nColNo++ > 0){
				sSQL += ", ";
			}
			sSQL += sColName + " = ?";
			aBindVars.add((String) oInput.get(sColName));
		}
		sSQL += " WHERE id" + sName + " = ?";
		aBindVars.add(oInput.get("id" + sName).toString());
		return sSQL;		
	}
	private String doInsert(ResultSet rs) throws Exception {
		int nColNo = 0;
		String sName = (String) oInput.get("archetype");
		String sSQL = "INSERT INTO " + sName + "(";
		String sValues = ") VALUES(";
		while(rs.next()){
			String sColName = rs.getString(1);
			if(nColNo++ > 0){
				sSQL += ", ";
				sValues += ", ";
			}
			sSQL += sColName;
			sValues += "?";
			aBindVars.add((String) oInput.get(sColName));
		}
		sSQL += sValues + ")";
		return sSQL;
	}
}
