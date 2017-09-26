package com.baudelaine.dd;

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
@WebServlet("/GetPKRelations")
public class GetPKRelationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPKRelationsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String table = request.getParameter("table");
		String alias = request.getParameter("alias");
		String type = request.getParameter("type");

		List<Object> result = new ArrayList<Object>();
		
		Connection con = null;
		ResultSet rst = null;
		DatabaseMetaData metaData = null;
		String schema = "";

		try {
			
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			
		    Map<String, Relation> map = new HashMap<String, Relation>();
			metaData = con.getMetaData();
			rst = metaData.getExportedKeys(con.getCatalog(), schema, table);
		    
		    
		    while (rst.next()) {
		    	
		    	String key_name = rst.getString("FK_NAME");
		    	String fk_name = rst.getString("FK_NAME");
		    	String pk_name = rst.getString("PK_NAME");
		    	String key_seq = rst.getString("KEY_SEQ");
		    	String fkcolumn_name = rst.getString("FKCOLUMN_NAME");
		    	String pkcolumn_name = rst.getString("PKCOLUMN_NAME");
		        String fktable_name = rst.getString("FKTABLE_NAME");
		        String pktable_name = rst.getString("PKTABLE_NAME");
		        String _id = key_name + "P";
		        
		        System.out.println("_id=" + _id);

		        if(!map.containsKey(_id)){
		        	
		        	System.out.println("+++ add relation +++");
		        	
		        	Relation relation = new Relation();
		        	
		        	relation.set_id(_id);
		        	relation.setKey_name(key_name);
		        	relation.setFk_name(fk_name);
		        	relation.setPk_name(pk_name);
		        	relation.setTable_name(pktable_name);
		        	relation.setPktable_name(fktable_name);
		        	relation.setPktable_alias(fktable_name);
		        	relation.setRelashionship("[" + type.toUpperCase() + "].[" + alias + "].[" + pkcolumn_name + "] = [" + fktable_name + "].[" + fkcolumn_name + "]");
		        	relation.setKey_type("P");
		        	
		        	Seq seq = new Seq();
		        	seq.setColumn_name(pkcolumn_name);
		        	seq.setPkcolumn_name(fkcolumn_name);
		        	seq.setKey_seq(Short.parseShort(key_seq));
		        	relation.addSeq(seq);
		        	
		        	map.put(_id, relation);

		        }
		        else{
		        	
		        	Relation relation = map.get(_id);
		        	if(!relation.getSeqs().isEmpty()){
			        	System.out.println("+++ update relation +++");
		        		Seq seq = new Seq();
			        	seq.setColumn_name(pkcolumn_name);
			        	seq.setPkcolumn_name(fkcolumn_name);
			        	seq.setKey_seq(Short.parseShort(key_seq));
			        	
			        	relation.addSeq(seq);
			        	
			        	StringBuffer sb = new StringBuffer((String) relation.getRelationship());
			        	sb.append(" AND [" + type.toUpperCase() + "].[" + alias + "].[" + pkcolumn_name + "] = [" + fktable_name + "].[" + fkcolumn_name + "]");
			        	relation.setRelashionship(sb.toString());
		        	}
		        	
		        }
	        	
		        	
		    }
		    
		    result = new ArrayList<Object>(map.values());

			
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
