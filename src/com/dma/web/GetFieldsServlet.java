package com.dma.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
		List<Field> result = new ArrayList<Field>();
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

		    rst = metaData.getIndexInfo(con.getCatalog(), schema, table, false, true);
		    Set<String> indexes = new HashSet<String>();
		    
		    while (rst.next()) {
		    	indexes.add(rst.getString("COLUMN_NAME"));
		    }

	        if(rst != null){rst.close();}
	        
	        rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
	        
	        while (rst.next()) {
	        	String field_name = rst.getString("COLUMN_NAME");
	        	String field_type = rst.getString("TYPE_NAME");
	        	String field_remarks = rst.getString("REMARKS");
	        	
	        	System.out.println(field_name + "," + field_type);
	        	Field field = new Field();
	        	field.setField_name(field_name);
	        	field.setField_type(field_type);
	        	field.setLabel(field_remarks);
	        	field.set_id(field_name + field_type);
	        	if(pks.contains(rst.getString("COLUMN_NAME"))){
	    			field.setPk(true);
	    		}
	        	if(indexes.contains(rst.getString("COLUMN_NAME"))){
	        		field.setIndex(true);
	        	}
	        	result.add(field);
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
