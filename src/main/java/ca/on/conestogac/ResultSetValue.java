package ca.on.conestogac;

import java.sql.*;

import org.json.simple.*;

import java.util.*;

public class ResultSetValue {
	public static String toJsonString(ResultSet oRs) throws SQLException{
    	//get metadata about SQL, which could be anything
    	ResultSetMetaData oMd = oRs.getMetaData();
    	int nCols = oMd.getColumnCount();
    	
    	//make a list to hold results
    	List<Map<String, String>> aResults = new LinkedList<Map<String, String>>();
        while(oRs.next()){

        	//for each row make a map
        	Map<String, String> oMap = new HashMap<String, String>();

        	//for each column (starting with 1!!!!!!) put a name value pair in the map
        	for(int n = 1; n <= nCols; n++){
        		oMap.put(oMd.getColumnLabel(n), oRs.getString(n));
        	}
        	aResults.add(oMap);
        }

		return JSONValue.toJSONString(aResults);
	}
}
