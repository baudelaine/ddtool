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
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, Object> dbmd = new HashMap<String, Object>();
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
		        
	        dbmd = (Map<String, Object>) request.getSession().getAttribute("dbmd");
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			dbEngine = (String) request.getSession().getAttribute("dbEngine");
			if((dbEngine).equalsIgnoreCase("ORA")){
				con.createStatement().execute("alter session set current_schema=" + schema);
			}

			List<String> tables = (List<String>) parms.get("tables");
			
			if(tables.size() > 0){
			
				Map<String, Object> tlMap = new HashMap<String, Object>();
				Map<String, Object> tdMap = new HashMap<String, Object>();
				Map<String, Object> clMap = new HashMap<String, Object>();
				Map<String, Object> cdMap = new HashMap<String, Object>();
				
				String tableInClause = "('" + StringUtils.join(tables.iterator(), "','") + "')";
				
				if(dbEngine.equalsIgnoreCase("ORA")){
					tableInClause = tableInClause.toLowerCase();
				}
	
				String tlQuery = (String) parms.get("tlQuery");
				System.out.println("tlQuery=" + tlQuery);
				if(!tlQuery.isEmpty() && StringUtils.countMatches(tlQuery, "(?)") == 1){
					tlQuery = StringUtils.replace(tlQuery, "(?)", tableInClause);
					System.out.println("tlQuery=" + tlQuery);
					stmt = con.prepareStatement(tlQuery);
					rst = stmt.executeQuery();
					while(rst.next()){
						tlMap.put(rst.getString(1).toUpperCase(), rst.getString(2));
					}
					rst.close();
					stmt.close();					
				}
				
				String tdQuery = (String) parms.get("tdQuery");
				System.out.println("tdQuery=" + tdQuery);
				if(!tdQuery.isEmpty() && StringUtils.countMatches(tdQuery, "(?)") == 1){
					tdQuery = StringUtils.replace(tdQuery, "(?)", tableInClause);
					stmt = con.prepareStatement(tdQuery);
					System.out.println("tdQuery=" + tdQuery);
					rst = stmt.executeQuery();
					while(rst.next()){
						tdMap.put(rst.getString(1).toUpperCase(), rst.getString(2));
					}
					rst.close();
					stmt.close();					
				}
				
				
				for(String table: tables){
//					System.out.println("table=" + table);

					List<String> fields = new ArrayList<String>();
					
					DatabaseMetaData metaData = con.getMetaData();
					rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
					while(rst.next()){
						fields.add(rst.getString("COLUMN_NAME"));
					}
					rst.close();

					String columnInClause = "('" + StringUtils.join(fields.iterator(), "','") + "')";
					if(dbEngine.equalsIgnoreCase("ORA")){
						columnInClause = columnInClause.toLowerCase();
						table = table.toLowerCase();
					}
					
					String clQuery = (String) parms.get("clQuery");
					if(!clQuery.isEmpty() && StringUtils.countMatches(clQuery, "(?)") == 1 && StringUtils.countMatches(clQuery, " ? ") == 1){
						clQuery = StringUtils.replace(clQuery, "(?)", columnInClause);
						
						Map<String, Object> cols = new HashMap<String, Object>();
						
						stmt = con.prepareStatement(clQuery);
						stmt.setString(1, table);
						rst = stmt.executeQuery();
						while(rst.next()){
							cols.put(rst.getString(2).toUpperCase(), rst.getString(3));
						}
						rst.close();
						stmt.close();
						
						clMap.put(table.toUpperCase(), cols);
					}
					
					String cdQuery = (String) parms.get("cdQuery");
					if(!cdQuery.isEmpty() && StringUtils.countMatches(cdQuery, "(?)") == 1 && StringUtils.countMatches(cdQuery, " ? ") == 1){
						cdQuery = StringUtils.replace(cdQuery, "(?)", columnInClause);
						
						Map<String, Object> cols = new HashMap<String, Object>();
						
						stmt = con.prepareStatement(cdQuery);
						stmt.setString(1, table);
						rst = stmt.executeQuery();
						while(rst.next()){
							cols.put(rst.getString(2).toUpperCase(), rst.getString(3));
						}
						rst.close();
						stmt.close();
						
						cdMap.put(table.toUpperCase(), cols);
					}
					
				}
				
				System.out.println("tlMap=" + tlMap);
				System.out.println("tdMap=" + tdMap);
				System.out.println("clMap=" + clMap);
				System.out.println("cdMap=" + cdMap);
				
				Map<String, Object> result = null;
				for(String table: tables){
					result = (Map<String, Object>) dbmd.get(table);
					if(result == null){
						result = new HashMap<String, Object>();
					}
					result.put("table_name", table);
					result.put("table_remarks", tlMap.get(table));
					result.put("table_description", tdMap.get(table));
					
					Map<String, Object> columns = (Map<String, Object>) result.get("columns");
					if(columns == null){
						columns = new HashMap<String, Object>();
					}
					
					Map<String, Object> cls = (Map<String, Object>) clMap.get(table);
					for(Entry<String, Object> cl: cls.entrySet()){
						String column_name = cl.getKey();
						Object column_remarks = cl.getValue();
						if(!columns.containsKey(cl.getKey())){
							columns.put(cl.getKey(), new HashMap<String, Object>());
						}
						((Map<String, Object>) columns.get(column_name)).put("column_remarks", column_remarks);
					}

					Map<String, Object> cds = (Map<String, Object>) cdMap.get(table);
					for(Entry<String, Object> cd: cds.entrySet()){
						String column_name = cd.getKey();
						Object column_description = cd.getValue();
						if(!columns.containsKey(cd.getKey())){
							columns.put(cd.getKey(), new HashMap<String, Object>());
						}
						((Map<String, Object>) columns.get(column_name)).put("column_description", column_description);
					}
					
					result.put("columns", columns);
					results.put(table, result);
				}
				
			}
			
			request.getSession().setAttribute("dbmd", results);
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
