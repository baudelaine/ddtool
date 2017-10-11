package com.baudelaine.dd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.HashSet;
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
@WebServlet("/GetFields")
public class GetFieldsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFieldsServlet() {
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
		Map<String, Object> result = new HashMap<String, Object>();
		String schema = "";

		try {
			
			String table = request.getParameter("table");
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			
		    DatabaseMetaData metaData = con.getMetaData();
		    
		    
		    rst = metaData.getPrimaryKeys(con.getCatalog(), schema, table);
		    Set<String> pks = new HashSet<String>();
		    
		    while (rst.next()) {
		    	pks.add(rst.getString("COLUMN_NAME"));
		    }

	        if(rst != null){rst.close();}

	        rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
        	ResultSetMetaData rsmd = rst.getMetaData();
        	
        	int colCount = rsmd.getColumnCount();
	        
	        while (rst.next()) {
        		Map<String, Object> datas = new HashMap<String, Object>();
    			datas.put("PK", false);
	        	for(int colNum = 1; colNum <= colCount; colNum++){
	        		String label = rsmd.getColumnLabel(colNum);
	        		Object value = rst.getObject(colNum);
	        		if(pks.contains(rst.getString("COLUMN_NAME"))){
	        			datas.put("PK", true);
	        		}
	        		datas.put(label, value);
	        	}
	        	result.put(rst.getString("COLUMN_NAME"), datas);
	        }		    
		    
	        if(rst != null){rst.close();}
	        
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
