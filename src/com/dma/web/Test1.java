package com.dma.web;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Test1 {

	public static void main(String[] args) throws SQLException{
		// TODO Auto-generated method stub

//		long l0 = 30;
//		long l1 = 100;
//		long result = (Math.round(((double)l0 / l1) * 100));
//		System.out.println((int) result);
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		String schema = "DANONE";
		List<Object> result = new ArrayList<Object>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/lvso112", "danone", "danone");
			con.createStatement().execute("alter session set current_schema=" + schema);
			
			List<String> tables = Arrays.asList(
					"S_SAMPLE" 
					);
			
			List<String> queries = Arrays.asList(
					"select systable.tableid, singular from systable join sdc on sdc.tableid = systable.tableid where systable.tableid = ?",
					"select systable.tableid, tabledoc from systable join sdc on sdc.tableid = systable.tableid where systable.tableid = ?",
					"select tableid, columnid, columnlabel from syscolumn where tableid = ? and columnid in (?)",
					"select tableid, columnid, columndoc from syscolumn where tableid = ? and columnid in (?)"
					);
			
			for(String table: tables){
				
				Map<String, Object> tbl = new HashMap<String, Object>();
				tbl.put("table_name", table);

				stmt = con.prepareStatement(queries.get(0));
				stmt.setString(1, table.toLowerCase());
				rst = stmt.executeQuery();
				while(rst.next()){
					tbl.put("table_remarks", rst.getString(1));
				}
				rst.close();
				stmt.close();
				
				stmt = con.prepareStatement(queries.get(1));
				stmt.setString(1, table.toLowerCase());
				rst = stmt.executeQuery ();
				while(rst.next()){
					tbl.put("table_description", rst.getString(1));
				}
				rst.close();
				stmt.close();
				
				DatabaseMetaData metaData = con.getMetaData();
				rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
				List<String> fields = new ArrayList<String>();
				while(rst.next()){
					String column_name = rst.getString("COLUMN_NAME");
					fields.add(column_name.toLowerCase());
				}
				
				List<Object> cols = new ArrayList<Object>();
				
				String in = "('" + StringUtils.join(fields.iterator(), "','") + "')";
				System.out.println("in=" + in);
				String qry  = StringUtils.replace(queries.get(3), "(?)", in);
				System.out.println(qry);
				
				stmt = con.prepareStatement(qry);
				stmt.setString(1, table.toLowerCase());
				rst = stmt.executeQuery();
				while(rst.next()){
					System.out.println(rst.getString(1) + "\t" + rst.getString(2) + "\t" + rst.getString(3));
				}
				rst.close();
				stmt.close();
				
				
//				while(rst.next()){
//					
//					Map<String, Object> col = new HashMap<String, Object>();
//					String column_name = rst.getString("COLUMN_NAME");
//					System.out.println("column_name=" + column_name);
//					col.put("column_name", column_name);
//					sb.append(",'" + column_name.toLowerCase() + "'");
					
//					stmt = con.prepareStatement(queries.get(2));
//					stmt.setString(1, table.toLowerCase());
//					stmt.setString(2, column_name.toLowerCase());
//					ResultSet subRst = stmt.executeQuery ();
//					while(subRst.next()){
//						System.out.println(subRst.getString(1));
//						col.put("column_remarks", subRst.getString(1));
//					}
//					subRst.close();
//					stmt.close();
//
//					stmt = con.prepareStatement(queries.get(3));
//					stmt.setString(1, table.toLowerCase());
//					stmt.setString(2, column_name.toLowerCase());
//					subRst = stmt.executeQuery ();
//					while(subRst.next()){
//						System.out.println(subRst.getString(1));
//						col.put("column_description", subRst.getString(1));
//					}
//					subRst.close();
//					stmt.close();
					
//					cols.add(col);
//				}
				
				rst.close();

				tbl.put("cols", cols);
				
				result.add(tbl);
	
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
