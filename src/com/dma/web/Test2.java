package com.dma.web;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test2 {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		String schema = "DANONE";
		List<Object> result = new ArrayList<Object>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/lvso112", "danone", "danone");
//			con.createStatement().execute("alter session set current_schema=" + schema);
			
			List<String> tables = Arrays.asList(
					"S_SAMPLE", 
					"sdidata"
					);
			
			for(String table: tables){
				
				DatabaseMetaData metaData = con.getMetaData();
				rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
				List<Object> cols = new ArrayList<Object>();
				while(rst.next()){
					
					Map<String, Object> col = new HashMap<String, Object>();
					String column_name = rst.getString("COLUMN_NAME");
					System.out.println("column_name=" + column_name);
					col.put("column_name", column_name);
					
					cols.add(col);
				}
				
				rst.close();
				
				result.add(cols);
	
			}

			System.out.println(result);
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(rst != null){
				rst.close();
			}
			if(stmt != null){
				stmt.close();
			}
			if(con != null){
				con.close();
			}
		}
		
		
	}

}
