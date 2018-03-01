package com.dma.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetTablesServlet
 */
@WebServlet("/GetSchema")
public class GetSchemaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSchemaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		List<Object> result = new ArrayList<Object>();
		String schema = "";

		try {
			
			String realPath = getServletContext().getRealPath("/");
			System.out.println("realPath=" + realPath);
			
			String fileName = realPath + "/res/schema.json";
			System.out.println("fileName=" + fileName);			
			File file = new File(fileName);
			
			if(file.exists()){
				
				System.out.println("Load schema from cache...");
				
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
			    ResultSet rst0 = metaData.getTables(con.getCatalog(), schema, "%", types);	
	
			    
			    while (rst0.next()) {
			    	
			    	String table_name = rst0.getString("TABLE_NAME");
			    	long recCount = 0;
		    		Statement stmt = null;
		    		ResultSet rs = null;
		            try{
			    		String query = "SELECT COUNT(*) FROM " + schema + "." + table_name;
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
			            if(rs != null){rs.close();}
						
					}
			    	
			    	if(recCount > 0){
				    	String table_type = rst0.getString("TABLE_TYPE");
				    	String table_remarks = rst0.getString("REMARKS");
				    	String table_schema = rst0.getString("TABLE_SCHEM");
				    	Map<String, Object> table = new HashMap<String, Object>();
				    	table.put("table_name", table_name);
				    	table.put("table_schema", table_schema);
				    	table.put("table_type", table_type);
				    	table.put("table_remarks", table_remarks);
				    	table.put("table_recCount", recCount);
				    	table.put("table_description", "");
					    
					    ResultSet rst1 = metaData.getColumns(con.getCatalog(), schema, table_name, "%");
					    List<Object> fields = new ArrayList<Object>();
					    while(rst1.next()){
						    Map<String, Object> field = new HashMap<>();
					    	field.put("column_name", rst1.getString("COLUMN_NAME"));
					    	field.put("column_type", rst1.getString("TYPE_NAME"));
					    	field.put("column_remarks", rst1.getString("REMARKS"));
				        	field.put("column_size", rst1.getInt("COLUMN_SIZE"));
					    	field.put("column_description", "");
				        	field.put("isNullable", rst1.getString("IS_NULLABLE"));
					    	field.put("filtered", false);
						    fields.add(field);
					    }
					    if(rst1 != null){rst1.close();}
					    table.put("columns", fields);
					    result.add(table);
			    	}
				    
			    }		    
			    
			    if(rst0 != null){rst0.close();}
			    
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
