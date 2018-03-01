package com.dma.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class GetTablesServlet
 */
@WebServlet("/GetSQLQuery")
public class GetSQLQueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSQLQueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		Map<String, Object> results = new HashMap<String, Object>();
		String schema = null;
		String dbEngine = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        Map<String, Object>	parms = new HashMap<String, Object>();
	        parms = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
		        
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			dbEngine = (String) request.getSession().getAttribute("dbEngine");
			if((dbEngine).equalsIgnoreCase("ORA")){
				con.createStatement().execute("alter session set current_schema=" + schema);
			}

			String query = (String) parms.get("query");
			results.put("query", query);
			results.put("schema", schema);
			
			System.out.println("query=" + query);
			
			stmt = con.prepareStatement(query);
			stmt.setMaxRows(20);
			rst = stmt.executeQuery();
			ResultSetMetaData rsmd = rst.getMetaData();
			int colCount = rsmd.getColumnCount();

			List<String> column_names = new ArrayList<String>();
			
			for(int colid = 1; colid <= colCount; colid++){
				column_names.add(rsmd.getColumnLabel(colid));
			}
			
			results.put("columns", column_names);
			
			List<Object> recs = new ArrayList<Object>();
			int i = 0;
			while(rst.next()){
				Map<String, Object> rec = new HashMap<String, Object>();
				for(int colid = 1; colid <= colCount; colid++){
					rec.put(column_names.get(colid -1), rst.getObject(colid));
				}
				recs.add(rec);
			}
			rst.close();
			stmt.close();					

			results.put("result", recs);
			
		    response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(results));
			
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
