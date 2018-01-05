package com.baudelaine.dd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetImportedKeysServlet
 */
@WebServlet("/GetQuerySubjects")
public class GetQuerySubjectsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	Connection con = null;
	DatabaseMetaData metaData = null;
	String schema = "";
	String table = "";
	String alias = "";
	String type = "";
	String qs_id = "";
	String r_id = "";
	String linker_id = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuerySubjectsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		table = request.getParameter("table");
		alias = request.getParameter("alias");
		type = request.getParameter("type");
//		qs_id = request.getParameter("qs_id");
//		r_id = request.getParameter("r_id");
		linker_id = request.getParameter("linker_id");
		
		System.out.println("table=" + table);
		System.out.println("alias=" + alias);
		System.out.println("type=" + type);
//		System.out.println("qs_id=" + qs_id);
//		System.out.println("r_id=" + r_id);
//		System.out.println("linker_id=" + linker_id);
		List<Object> result = new ArrayList<Object>();

		try{
			
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			metaData = con.getMetaData();
			
//			Map<String, QuerySubject> query_subjects = (Map<String, QuerySubject>) request.getSession().getAttribute("query_subjects");
//			
//			QuerySubject querySubject = query_subjects.get(alias + type);
//			
//			if(querySubject == null){

			QuerySubject querySubject = getQuerySubjects();

			querySubject.setFields(getFields());
			querySubject.addRelations(getForeignKeys());
				
//			}
			
//			if(linker_id != null && linker_id.length() > -1){
//				querySubject.addLinker_id(linker_id);
//			}
//			
//			query_subjects.put(querySubject.get_id(), querySubject);
			
			// Update fin/ref to true of the linker relation
//			QuerySubject linker = query_subjects.get(qs_id);
//			if(linker != null){
//				Map<String, Relation> linkerRelations = linker.getRelations();
//				Relation linkerRelation = linkerRelations.get(r_id);
//				if(type.equalsIgnoreCase("Final")){
//					linkerRelation.setFin(true);
//				}
//				if(type.equalsIgnoreCase("Ref")){
//					linkerRelation.setRef(true);
//				}
//				linker.incRelationCount(alias + type);
//			}
//			
//
//			for(Entry<String, QuerySubject> query_subject : query_subjects.entrySet()){
//		    	result.add(query_subject.getValue());
//		    }
			
			result.add(querySubject);
			
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
	
	protected QuerySubject getQuerySubjects() throws SQLException{
		
		String[] types = {"TABLE"};
		ResultSet rst = metaData.getTables(con.getCatalog(), schema, table, types);
		String label = "";
		
		while (rst.next()) {
	    	label = rst.getString("REMARKS");
	    }
		
		if(rst != null){rst.close();}
    	
		QuerySubject result = new QuerySubject();
		
		result.set_id(alias + type);
		result.setTable_alias(alias);
		result.setTable_name(table);
		result.setType(type);
		result.setLabel(label);
		result.addLinker_id(linker_id);
        
        return result;
        
	}
	
	protected List<Field> getFields() throws SQLException{
		
//		Map<String, Field> result = new HashMap<String, Field>();
		
		ResultSet rst = metaData.getPrimaryKeys(con.getCatalog(), schema, table);
	    Set<String> pks = new HashSet<String>();
	    
	    while (rst.next()) {
	    	pks.add(rst.getString("COLUMN_NAME"));
	    }

        if(rst != null){rst.close();}
        
		List<Field> result = new ArrayList<Field>();
		
        rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
        
        while (rst.next()) {
        	String field_name = rst.getString("COLUMN_NAME");
        	String field_type = rst.getString("TYPE_NAME");
//        	System.out.println(field_name + "," + field_type);
        	Field field = new Field();
        	field.setField_name(field_name);
        	field.setField_type(field_type);
        	field.setLabel(rst.getString("REMARKS"));
        	field.setField_size(rst.getInt("COLUMN_SIZE"));
        	field.setNullable(rst.getString("IS_NULLABLE"));
        	field.set_id(field_name + field_type);
        	if(pks.contains(rst.getString("COLUMN_NAME"))){
    			field.setPk(true);
    		}
        	result.add(field);
        }

        if(rst != null){rst.close();}
        
		return result;
		
	}
	
	protected List<Relation> getForeignKeys() throws SQLException{
		
		Map<String, Relation> map = new HashMap<String, Relation>();
		
	    ResultSet rst = metaData.getImportedKeys(con.getCatalog(), schema, table);
	    
	    while (rst.next()) {
	    	
	    	String key_name = rst.getString("FK_NAME");
	    	String fk_name = rst.getString("FK_NAME");
	    	String pk_name = rst.getString("PK_NAME");
	    	String key_seq = rst.getString("KEY_SEQ");
	    	String fkcolumn_name = rst.getString("FKCOLUMN_NAME");
	    	String pkcolumn_name = rst.getString("PKCOLUMN_NAME");
	        String fktable_name = rst.getString("FKTABLE_NAME");
	        String pktable_name = rst.getString("PKTABLE_NAME");
	        String _id = key_name + "F";
	        
	        System.out.println("_id=" + _id);

	        if(!map.containsKey(_id)){
	        	
	        	System.out.println("+++ add relation +++");
	        	
	        	Relation relation = new Relation();
	        	
	        	relation.set_id(_id);
	        	relation.setKey_name(key_name);
	        	relation.setFk_name(fk_name);
	        	relation.setPk_name(pk_name);
	        	relation.setTable_name(fktable_name);
	        	relation.setPktable_name(pktable_name);
	        	relation.setPktable_alias(pktable_name);
	        	relation.setRelashionship("[" + type.toUpperCase() + "].[" + alias + "].[" + fkcolumn_name + "] = [" + pktable_name + "].[" + pkcolumn_name + "]");
	        	relation.setKey_type("F");
	        	
	        	Seq seq = new Seq();
	        	seq.setColumn_name(fkcolumn_name);
	        	seq.setPkcolumn_name(pkcolumn_name);
	        	seq.setKey_seq(Short.parseShort(key_seq));
	        	relation.addSeq(seq);
	        	
	        	map.put(_id, relation);

	        }
	        else{
	        	
	        	Relation relation = map.get(_id);
	        	if(!relation.getSeqs().isEmpty()){
		        	System.out.println("+++ update relation +++");
	        		Seq seq = new Seq();
		        	seq.setColumn_name(fkcolumn_name);
		        	seq.setPkcolumn_name(pkcolumn_name);
		        	seq.setKey_seq(Short.parseShort(key_seq));
		        	
		        	relation.addSeq(seq);
		        	
		        	StringBuffer sb = new StringBuffer((String) relation.getRelationship());
		        	sb.append(" AND [" + type.toUpperCase() + "].[" + alias + "].[" + fkcolumn_name + "] = [" + pktable_name + "].[" + pkcolumn_name + "]");
		        	relation.setRelashionship(sb.toString());
	        	}
	        	
	        }
        	
	        	
	    }
	    
	    return new ArrayList<Relation>(map.values());
		
	}
	
	protected List<Relation> getPrimaryKeys() throws SQLException{
		
	    Map<String, Relation> map = new HashMap<String, Relation>();
		
		ResultSet rst = null;
		DatabaseMetaData metaData = con.getMetaData();
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
	    
	    return new ArrayList<Relation>(map.values());
		
	}
	

}

