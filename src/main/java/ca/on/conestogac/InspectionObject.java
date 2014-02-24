package ca.on.conestogac;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.*;

public class InspectionObject {
	JSONObject oInput = null;
	public InspectionObject(String sInput)
	{
		oInput = (JSONObject)JSONValue.parse(sInput);
	}
	public void save() throws Exception
	{
		if(oInput.get("id" + oInput.get("archetype")) == null){
			doInsert();
		}
		else{
			doUpdate();
		}
	}
	private void doUpdate() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("not implemented");
		
	}
	private void doInsert() throws Exception {
		Connection insConnection = null;
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
			oStmt = insConnection.prepareStatement("SELECT name FROM idDispAttribute where idDispClass = ?");
			oStmt.setLong(1, idDispClass);
			rs = oStmt.executeQuery();
			int nColNo = 1;
			while(rs.next()){
				String sColName = rs.getString(1);
			}
		}finally{
			if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
            if (oStmt != null) try { oStmt.close(); } catch (SQLException logOrIgnore) {}        	
            if (insConnection != null) try { insConnection.close(); } catch (SQLException logOrIgnore) {}			
			
		}
		throw new Exception("not implemented");
	}
}
