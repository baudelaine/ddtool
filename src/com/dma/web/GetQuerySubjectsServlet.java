package com.dma.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	boolean withRecCount = false;
	long qs_recCount = 0L;
	Map<String, Object> dbmd = null;
       
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
		linker_id = request.getParameter("linker_id");
		
		System.out.println("table=" + table);
		System.out.println("alias=" + alias);
		System.out.println("type=" + type);
		List<Object> result = new ArrayList<Object>();

		try{
			
			withRecCount = (Boolean) request.getSession().getAttribute("withRecCount");
			System.out.println("withRecCount=" + withRecCount);
			con = (Connection) request.getSession().getAttribute("con");
			schema = (String) request.getSession().getAttribute("schema");
			dbmd = (Map<String, Object>) request.getSession().getAttribute("dbmd");
			metaData = con.getMetaData();
			
			QuerySubject querySubject = getQuerySubjects();
			
			querySubject.setFields(getFields());
			querySubject.addRelations(getForeignKeys());
				
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
		
		if(withRecCount){
            long recCount = 0;
    		Statement stmt = null;
    		ResultSet rs = null;
            try{
	    		String query = "SELECT COUNT(*) FROM " + schema + "." + table;
	    		stmt = con.createStatement();
	            rs = stmt.executeQuery(query);
	            while (rs.next()) {
	            	recCount = rs.getLong(1);
	            }
	            result.setRecCount(recCount);
	            qs_recCount = recCount;
            }
            catch(SQLException e){
            	System.out.println("CATCHING SQLEXEPTION...");
            	System.out.println(e.getSQLState());
            	System.out.println(e.getMessage());
            	
            }
            finally {
	            if (stmt != null) { stmt.close();}
	            if(rst != null){rst.close();}
				
			}
			
		}
		
		if(dbmd != null){
			@SuppressWarnings("unchecked")
			Map<String, Object> o = (Map<String, Object>) dbmd.get(table);
			result.setLabel((String) o.get("table_remarks"));
			result.setDescription((String) o.get("table_description"));
		}
        
        return result;
        
	}
	
	@SuppressWarnings("unchecked")
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
        
        Map<String, Object> table_labels = null;
        Map<String, Object> columns = null;
        if(dbmd != null){
			table_labels = (Map<String, Object>) dbmd.get(table);
			columns = (Map<String, Object>) table_labels.get("columns");
        }
		
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
    		if(columns != null){
    			Map<String, Object> column = (Map<String, Object>) columns.get(field_name); 
    			field.setLabel((String) column.get("column_remarks"));
    			field.setDescription((String) column.get("column_description"));
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
	        	relation.setTable_alias(alias);
	        	relation.setPktable_name(pktable_name);
	        	relation.setPktable_alias(pktable_name);
	        	relation.setRelashionship("[" + type.toUpperCase() + "].[" + alias + "].[" + fkcolumn_name + "] = [" + pktable_name + "].[" + pkcolumn_name + "]");
	        	relation.setWhere(fktable_name + "." + fkcolumn_name + " = " + pktable_name + "." + pkcolumn_name);
	        	relation.setKey_type("F");
	        	relation.setType(type.toUpperCase());
	        	relation.set_id("FK_" + relation.getPktable_alias() + "_" + alias + "_" + type.toUpperCase());
	        	
	        	String[] types = {"TABLE"};
	    		ResultSet rst0 = metaData.getTables(con.getCatalog(), schema, pktable_name, types);
	    		while (rst0.next()) {
	    	    	relation.setLabel(rst0.getString("REMARKS"));
	    	    }
	    		if(rst0 != null){rst0.close();}
	        	
	    		if(dbmd != null){
	    			@SuppressWarnings("unchecked")
	    			Map<String, Object> o = (Map<String, Object>) dbmd.get(pktable_name);
	    			if(o != null){
		    			relation.setLabel((String) o.get("table_remarks"));
		    			relation.setDescription((String) o.get("table_description"));
	    			}
	    		}
	        	
	        	
	        	Seq seq = new Seq();
	        	seq.setTable_name(fktable_name);
	        	seq.setPktable_name(pktable_name);
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
		        	seq.setTable_name(fktable_name);
		        	seq.setPktable_name(pktable_name);
		        	seq.setColumn_name(fkcolumn_name);
		        	seq.setPkcolumn_name(pkcolumn_name);
		        	seq.setKey_seq(Short.parseShort(key_seq));
		        	
		        	relation.addSeq(seq);
		        	
		        	StringBuffer sb = new StringBuffer((String) relation.getRelationship());
		        	sb.append(" AND [" + type.toUpperCase() + "].[" + alias + "].[" + fkcolumn_name + "] = [" + pktable_name + "].[" + pkcolumn_name + "]");
		        	relation.setRelashionship(sb.toString());
		        	
		        	sb = new StringBuffer((String) relation.getWhere());
		        	sb.append(" AND " + fktable_name + "." + fkcolumn_name + " = " + pktable_name + "." + pkcolumn_name);
		        	relation.setWhere(sb.toString());
		        	
	        	}
	        	
	        }
        	
	        	
	    }
	    
	    if(withRecCount){
	    	for(Entry<String, Relation> relation: map.entrySet()){
	    		Relation rel = relation.getValue();
	    		
	    		Set<String> tableSet = new HashSet<String>();
	    		for(Seq seq: rel.getSeqs()){
	    			tableSet.add(schema + "." + seq.pktable_name);
	    			tableSet.add(schema + "." + seq.table_name);
	    		}
	    		
	    		System.out.println("tableSet=" + tableSet);
	    		
	    		StringBuffer sb = new StringBuffer();;
	    		
	    		for(String table: tableSet){
	    			sb.append(", " + table);
	    		}
	    		String tables = sb.toString().substring(1);
	    		
	            long recCount = 0;
	    		Statement stmt = null;
	    		ResultSet rs = null;
	            try{
		    		String query = "SELECT COUNT(*) FROM " + tables + " WHERE " + rel.where;
		    		System.out.println(query);
		    		stmt = con.createStatement();
		            rs = stmt.executeQuery(query);
		            while (rs.next()) {
		            	recCount = rs.getLong(1);
		            }
		            rel.setRecCount(recCount);
		    		long result = (Math.round(((double)recCount / qs_recCount) * 100));
		            rel.setRecCountPercent((int) result);
	            }
	            catch(SQLException e){
	            	System.out.println("CATCHING SQLEXEPTION...");
	            	System.out.println(e.getSQLState());
	            	System.out.println(e.getMessage());
	            	
	            }
	            finally {
		            if (stmt != null) { stmt.close();}
		            if(rst != null){rst.close();}
					
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
	        	relation.setTable_alias(alias);
	        	relation.setPktable_name(fktable_name);
	        	relation.setPktable_alias(fktable_name);
	        	relation.setRelashionship("[" + type.toUpperCase() + "].[" + alias + "].[" + pkcolumn_name + "] = [" + fktable_name + "].[" + fkcolumn_name + "]");
	        	relation.setKey_type("P");
	        	relation.setType(type.toUpperCase());
	        	relation.set_id("PK_" + relation.getPktable_alias() + "_" + alias + "_" + type.toUpperCase());
	        	
	        	String[] types = {"TABLE"};
	    		ResultSet rst0 = metaData.getTables(con.getCatalog(), schema, pktable_name, types);
	    		while (rst0.next()) {
	    	    	relation.setLabel(rst0.getString("REMARKS"));
	    	    }
	    		if(rst0 != null){rst0.close();}
	        	
	    		if(dbmd != null){
	    			@SuppressWarnings("unchecked")
	    			Map<String, Object> o = (Map<String, Object>) dbmd.get(pktable_name);
	    			if(o != null){
		    			relation.setLabel((String) o.get("table_remarks"));
		    			relation.setDescription((String) o.get("table_description"));
	    			}
	    		}
	        	
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
	    
	    if(withRecCount){
	    	for(Entry<String, Relation> relation: map.entrySet()){
	    		Relation rel = relation.getValue();
	    		
	    		Set<String> tableSet = new HashSet<String>();
	    		for(Seq seq: rel.getSeqs()){
	    			tableSet.add(schema + "." + seq.pktable_name);
	    			tableSet.add(schema + "." + seq.table_name);
	    		}
	    		
	    		System.out.println("tableSet=" + tableSet);
	    		
	    		StringBuffer sb = new StringBuffer();;
	    		
	    		for(String table: tableSet){
	    			sb.append(", " + table);
	    		}
	    		String tables = sb.toString().substring(1);
	    		
	            long recCount = 0;
	    		Statement stmt = null;
	    		ResultSet rs = null;
	            try{
		    		String query = "SELECT COUNT(*) FROM " + tables + " WHERE " + rel.where;
		    		System.out.println(query);
		    		stmt = con.createStatement();
		            rs = stmt.executeQuery(query);
		            while (rs.next()) {
		            	recCount = rs.getLong(1);
		            }
		            rel.setRecCount(recCount);
		    		long result = (Math.round(((double)recCount / qs_recCount) * 100));
		            rel.setRecCountPercent((int) result);
	            }
	            catch(SQLException e){
	            	System.out.println("CATCHING SQLEXEPTION...");
	            	System.out.println(e.getSQLState());
	            	System.out.println(e.getMessage());
	            	
	            }
	            finally {
		            if (stmt != null) { stmt.close();}
		            if(rst != null){rst.close();}
					
				}
	    		
	    	}
	    }
	    
	    
	    return new ArrayList<Relation>(map.values());
		
	}
	

}

