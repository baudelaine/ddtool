package com.baudelaine.dd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
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
			    Map<String, String> table = new HashMap<String, String>();
			    table.put("name", table_name);
			    table.put("type", table_type);
			    temp.add(table);
		    }
		    
		    rst.close();
		    
		    for(Map<String, String> table: temp){
		    	String tableName = table.get("name");
		    	String tableType = table.get("type");
		    	rst = metaData.getImportedKeys(con.getCatalog(), schema, table.get("name"));
		    	int seqCount = 0;
		    	Set<String> FKSet = new HashSet<String>();
		    	Map<String, Object> scan = new HashMap<String, Object>();
		    	while(rst.next()){
		    		String keyName = rst.getString("FK_NAME");
		    		FKSet.add(keyName);
		    		seqCount++;
		    	}
	    		System.out.println(tableName + " -> " + FKSet.size() + " -> " + seqCount);
	    		scan.put("name", tableName);
	    		scan.put("type", tableType);
	    		scan.put("keyCount", FKSet.size());
	    		scan.put("seqCount", seqCount);
	    		result.add(scan);
			    rst.close();
		    	
		    }
		    
		    
		    response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(result));
			
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
