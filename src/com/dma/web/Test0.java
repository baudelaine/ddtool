package com.dma.web;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCDatabaseMetaData;

public class Test0 {

	public static void main(java.lang.String[] args){
		
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			System.out.println("com.ibm.as400.access.AS400JDBCDriver loaded successfully !!!");

			AS400JDBCConnection con = (AS400JDBCConnection) DriverManager.getConnection("jdbc:as400://172.16.189.40", "SEB", "SEB");
			AS400JDBCDatabaseMetaData metaData = (AS400JDBCDatabaseMetaData) con.getMetaData();
			
			Statement stmt = con.createStatement();		
			ResultSet rst = null;
			String query = "";

			query = "SELECT constraint_name, constraint_type, table_name FROM qsys2.syscst WHERE table_schema = 'FG'";
			//query = "SELECT * FROM qsys2.sysschemas";
			query = "SELECT * FROM qsys2.syscst WHERE table_schema = 'FG'";
			//query = "select * from LAMFIC_V3.ANA5PF FETCH FIRST 1 ROWS ONLY";

                        rst = metaData.getCatalogs(); 
                        while (rst.next()) { 
                                System.out.println("catalog: " + rst.getString("TABLE_CAT")); 
                        }
                        if(rst != null) rst.close();

			//rst =  metaData.getImportedKeys("S6514BFA", "LAMFIC_V3", "ANA5PF");
			rst = stmt.executeQuery(query);
			System.out.println(query);
			System.out.println("rst=" + rst);
			
			ResultSetMetaData rsmd = rst.getMetaData();
			int colCount = rsmd.getColumnCount();
			System.out.println("rsmd.getColumunCount()=" + colCount);
			
			for(int col = 1; col <= colCount; col++){
				System.out.print(rsmd.getColumnLabel(col) + ";");
			}
			System.out.println("\n----------------------------------------");
			while(rst.next()){
				for(int col = 1; col <= colCount; col++){
					System.out.print(rst.getString(col) + ";");
				}
				System.out.println("");
			}
			System.out.println("\n----------------------------------------");
			
                        if(rst != null) rst.close();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
