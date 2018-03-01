package com.dma.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetTablesServlet
 */
@WebServlet("/Scan")
public class ScanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScanServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		ResultSet rst = null;
		List<Object> result = new ArrayList<Object>();
		String schema = "";

		try {
			
			String realPath = getServletContext().getRealPath("/");
			System.out.println("realPath=" + realPath);
			
			String fileName = realPath + "/res/tables.json";
			System.out.println("fileName=" + fileName);			
			File file = new File(fileName);
			
			if(file.exists()){
			
				BufferedReader br = new BufferedReader(new FileReader(file));
			
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
	
				String line;
				while((line = br.readLine()) != null){
					response.getWriter().write(line);
				}
				
				if(br != null){br.close();}
			}
			else{
			
				con = (Connection) request.getSession().getAttribute("con");
				schema = (String) request.getSession().getAttribute("schema");
				
			    DatabaseMetaData metaData = con.getMetaData();
			    //String[] types = {"TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"};
			    String[] types = {"TABLE"};
			    rst = metaData.getTables(con.getCatalog(), schema, "%", types);	
	
			    List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
			    
			    while (rst.next()) {
			    	String table_name = rst.getString("TABLE_NAME");
			    	String table_type = rst.getString("TABLE_TYPE");
			    	String table_remarks = rst.getString("REMARKS");
				    Map<String, String> table = new HashMap<String, String>();
				    table.put("name", table_name);
				    table.put("type", table_type);
				    table.put("remarks", table_remarks);
				    temp.add(table);
			    }
			    
			    if(rst != null){rst.close();}
			    
			    for(Map<String, String> table: temp){
			    	String tableName = table.get("name");
			    	String tableType = table.get("type");
			    	String tableRemarks = table.get("remarks");
			    	Map<String, Object> scan = new HashMap<String, Object>();
			    	
			    	rst = metaData.getImportedKeys(con.getCatalog(), schema, table.get("name"));
			    	int FKSeqCount = 0;
			    	Set<String> FKSet = new HashSet<String>();
			    	while(rst.next()){
			    		String FKName = rst.getString("FK_NAME");
			    		FKSet.add(FKName);
			    		FKSeqCount++;
			    	}
		            if(rst != null){rst.close();}
	
			    	rst = metaData.getExportedKeys(con.getCatalog(), schema, table.get("name"));
			    	int PKSeqCount = 0;
			    	Set<String> PKSet = new HashSet<String>();
			    	while(rst.next()){
			    		String PKName = rst.getString("FK_NAME");
			    		PKSet.add(PKName);
			    		PKSeqCount++;
			    	}
		            if(rst != null){rst.close();}

				    rst = metaData.getPrimaryKeys(con.getCatalog(), schema, table.get("name"));
				    Set<String> pks = new HashSet<String>();
				    
				    while (rst.next()) {
				    	pks.add(rst.getString("COLUMN_NAME"));
				    }

			        if(rst != null){rst.close();}

				    rst = metaData.getIndexInfo(con.getCatalog(), schema, table.get("name"), false, true);
				    Set<String> indexes = new HashSet<String>();
				    
				    while (rst.next()) {
				    	indexes.add(rst.getString("COLUMN_NAME"));
				    }

			        if(rst != null){rst.close();}		            
		            long recCount = 0;
		    		Statement stmt = null;
		    		ResultSet rs = null;
		            try{
			    		String query = "SELECT COUNT(*) FROM " + schema + "." + table.get("name");
			    		stmt = con.createStatement();
			            rs = stmt.executeQuery(query);
			            while (rs.next()) {
			            	recCount = rs.getLong(1);
			            }
		            }
		            catch(SQLException e){
		            	System.out.println("CATCHING SQLEXEPTION...");
		            	System.out.println(e.getSQLState());
		            	System.out.println(e.getMessage());
		            	
		            }
		            finally {
			            if (stmt != null) { stmt.close();}
			            if(rst != null){rst.close();}
						
					}
		            
		            if(recCount > 0){
			            scan.put("name", tableName);
			    		scan.put("type", tableType);
			    		scan.put("remarks", tableRemarks);
			    		scan.put("FKCount", FKSet.size());
			    		scan.put("PKCount", PKSet.size());
			    		scan.put("FKSeqCount", FKSeqCount);
			    		scan.put("PKSeqCount", PKSeqCount);
			    		scan.put("RecCount", recCount);
			    		scan.put("PKFieldsCount", pks.size());
			    		scan.put("IndexFieldsCount", indexes.size());
			    		result.add(scan);
		            }
			    	
			    }
			    
			    response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(Tools.toJSON(result));
			}
			
		}
		catch (Exception e){
			e.printStackTrace(System.err);
		}		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
