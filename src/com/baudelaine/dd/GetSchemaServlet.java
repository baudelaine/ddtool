package com.baudelaine.dd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
		ResultSet rst0 = null;
		ResultSet rst1 = null;
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
			    rst0 = metaData.getTables(con.getCatalog(), schema, "%", types);	
	
			    
			    while (rst0.next()) {
			    	String table_name = rst0.getString("TABLE_NAME");
			    	String table_type = rst0.getString("TABLE_TYPE");
			    	String table_remarks = rst0.getString("REMARKS");
				    Map<String, Object> table = new HashMap<>();
				    table.put("tabName", table_name);
				    table.put("tabType", table_type);
				    table.put("tabRemarks", table_remarks);
				    
				    rst1 = metaData.getColumns(con.getCatalog(), schema, table_name, "%");
				    while(rst1.next()){
				    	table.put("colName", rst1.getString("COLUMN_NAME"));
				    	table.put("colType", rst1.getString("TYPE_NAME"));
				    	table.put("colRemarks", rst1.getString("REMARKS"));
				    }
				    if(rst1 != null){rst1.close();}
				    
				    result.add(table);
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
