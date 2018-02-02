package com.baudelaine.dd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class GetTablesServlet
 */
@WebServlet("/GetLabels")
public class GetLabelsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLabelsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String schema = "";

		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        String json = "";
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        Map<String, Object>	parms = new HashMap<String, Object>();
	        parms = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
			
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			Statement stmt = con.createStatement();
			stmt.execute("ALTER SESSION SET CURRENT_SCHEMA=DANONE");

			@SuppressWarnings("unchecked")
			List<String> tables = (List<String>) parms.get("tables");
			
			PreparedStatement getTableLabel = null;
			
			
//			String queryTableLabel = (String) parms.get("tlQuery");
			String queryTableLabel = "select singular from systable join sdc on sdc.tableid = systable.tableid where systable.tableid = ?";
			
			try {
				getTableLabel = con.prepareStatement(queryTableLabel);
				for(String table: tables){
					System.out.println("table=" + table);
					getTableLabel.setString(1, table);
					ResultSet tlRst = getTableLabel.executeQuery("select singular from systable join sdc on sdc.tableid = systable.tableid where systable.tableid = 'S_SAMPLE'");
					while(tlRst.next()){
						System.out.println("tlRst.getString(1)=" + tlRst.getString(1));
						result.put(table, tlRst.getString(1));
					}
				}
				
			}
			catch(SQLException e){
				e.printStackTrace(System.err);
			}
			finally {
				if(getTableLabel != null){
					getTableLabel.close();
				}
			}
			
//		    DatabaseMetaData metaData = con.getMetaData();
//		    
//	        rst = metaData.getColumns(con.getCatalog(), schema, "%", "%");
//	        
//	        while (rst.next()) {
//	        	String field_name = rst.getString("COLUMN_NAME");
//	        	String field_type = rst.getString("TYPE_NAME");
//	        	System.out.println(field_name + "," + field_type);
//	        	Field field = new Field();
//	        	field.setField_name(field_name);
//	        	field.setField_type(field_type);
//	        	field.set_id(field_name + field_type);
//	        	result.add(field);
//	        }
//	        
//		    
//	        if(rst != null){rst.close();}
	        
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
