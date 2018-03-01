package com.dma.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetImportedKeysServlet
 */
@WebServlet("/GetExportedKeys")
public class GetExportedKeysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetExportedKeysServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String table = request.getParameter("table");
		String alias = request.getParameter("alias");
		String type = request.getParameter("type");
		System.out.println("table=" + table);
		System.out.println("alias=" + alias);
		System.out.println("type=" + type);
		Connection con = null;
		ResultSet rst = null;
		List<Object> result = new ArrayList<Object>();
    	String schema = "";
		
		try {
			
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			DatabaseMetaData metaData = con.getMetaData();
		    rst = metaData.getExportedKeys(con.getCatalog(), schema, table);
	    	Map<String, Object> keys = new HashMap<>();
		    while (rst.next()) {
		    	
		    	String key_name = rst.getString("FK_NAME");
		    	String fk_name = rst.getString("FK_NAME");
		    	String pk_name = rst.getString("PK_NAME");
		    	String key_seq = rst.getString("KEY_SEQ");
		    	String fkcolumn_name = rst.getString("FKCOLUMN_NAME");
		    	String pkcolumn_name = rst.getString("PKCOLUMN_NAME");
		        String fktable_name = rst.getString("FKTABLE_NAME");
		        String pktable_name = rst.getString("PKTABLE_NAME");

		        if(!keys.containsKey(key_name)){
		        	Map<String, Object> key = new HashMap<String, Object>();
		        	key.put("_rev", null);
		        	key.put("_id", key_name + "Final" + alias + "P");
		        	key.put("key_name", key_name);
		        	key.put("fk_name", fk_name);
		        	key.put("pk_name", pk_name);
    		    	key.put("table_name", pktable_name);
    		    	key.put("pktable_name", fktable_name);	
    		    	key.put("table_alias", alias);
    		    	key.put("pktable_alias", fktable_name);
    		    	key.put("type", type);
    		    	key.put("fin", false);
    		    	key.put("ref", false);
    		    	key.put("relashionship", "[" + fktable_name + "].[" + fkcolumn_name + "] = [" + pktable_name + "].[" + pkcolumn_name + "]");
    		    	key.put("linker", false);
    		    	key.put("linker_ids", new HashSet<String>());
    		    	key.put("key_type", "P");
		        	
		        	List<Object> seqs = new  ArrayList<Object>();
		        	
		        	Map<String, Object> seq = new HashMap<>();
    		    	seq.put("column_name", pkcolumn_name);
    		    	seq.put("pkcolumn_name", fkcolumn_name);
    		    	seq.put("key_seq", key_seq);	

    		    	seqs.add(seq);
    		    	
		        	key.put("seqs", seqs);
		        	keys.put(key_name, key);
		        }
		        else{
		        	
		        	Map<String, Object> fk 	= (Map<String, Object>) keys.get(key_name);
		        	List<Object> seqs = (List<Object>) fk.get("seqs");
		        	if(seqs != null){
			        	Map<String, Object> seq = new HashMap<>();
	    		    	seq.put("column_name", pkcolumn_name);
	    		    	seq.put("pkcolumn_name", fkcolumn_name);
	    		    	seq.put("key_seq", key_seq);
	    		    	seqs.add(seq);
	    		    	StringBuffer sb = new StringBuffer((String) fk.get("relashionship"));
	    		    	sb.append(" AND [" + fktable_name + "].[" + fkcolumn_name + "] = [" + pktable_name + "].[" + pkcolumn_name + "]");
	    		    	fk.put("relashionship", sb.toString());
	
		        	}
		        }
	        	
		        	
		    }
		    
		    for(Entry<String, Object> fk: keys.entrySet()){
		    	result.add(fk.getValue());
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

