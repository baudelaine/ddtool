package com.baudelaine.dd;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCDatabaseMetaData;

public class Test0 {

	public static void main(java.lang.String[] args){
		
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			System.out.println("com.ibm.as400.access.AS400JDBCDriver loaded successfully !!!");

			AS400JDBCConnection con = (AS400JDBCConnection) DriverManager.getConnection("jdbc:as400://172.16.2.70", "IBMIIC", "spcspc");
			AS400JDBCDatabaseMetaData metaData = (AS400JDBCDatabaseMetaData) con.getMetaData();
			
			
			ResultSet rst =  metaData.getExportedKeys(null, "LAMFIV_V3", "ANA5PF");
			
			ResultSetMetaData rsmd = rst.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(rst.next()){
				for(int col = 1; col <= colCount; col++){
					System.out.println(rsmd.getColumnLabel(col));
					System.out.println(rst.getString(col));
				}
			}
			
			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
