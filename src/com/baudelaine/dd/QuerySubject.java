package com.baudelaine.dd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuerySubject {
	
	String _id = "";
	String _ref = null;
	String table_name = "";
	String table_alias = "";
	String type = "";
	boolean visible = false;
	String filter = "";
	String Label = "";
	boolean linker = false;
	Set<String> linker_ids =  new HashSet<String>();
	List<Field> fields = new ArrayList<Field>();
	List<Relation> relations = new ArrayList<Relation>();
	Map<String, Integer> relationCount = new HashMap<String, Integer>(); 
	int recurseCount = 1;
	
	public void incRelationCount(String qs_id){
		if(relationCount.get(qs_id) == null){
			relationCount.put(qs_id, new Integer(1));
		}
		else{
			int count = relationCount.get(qs_id);
			relationCount.put(qs_id, ++count);
		}
	}
	public Map<String, Integer> getRelationCount() {
		return relationCount;
	}
	public void setRelationCount(Map<String, Integer> relationCount) {
		this.relationCount = relationCount;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_ref() {
		return _ref;
	}
	public void set_ref(String _ref) {
		this._ref = _ref;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTable_alias() {
		return table_alias;
	}
	public void setTable_alias(String table_alias) {
		this.table_alias = table_alias;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public boolean isLinker() {
		return linker;
	}
	public void setLinker(boolean linker) {
		this.linker = linker;
	}
	public Set<String> getLinker_ids() {
		return linker_ids;
	}
	public void setLinker_ids(Set<String> linker_ids) {
		this.linker_ids = linker_ids;
	}
	public void addLinker_id(String linker_id){
		this.linker_ids.add(linker_id);
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public void addField(Field field){
		this.fields.add(field);
	}
	public void addFields(List<Field> fields){
		this.fields.addAll(fields);
	}
	public List<Relation> getRelations() {
		return relations;
	}
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	} 
	public void addRelation(String _id, Relation relation){
		this.relations.add(relation);
	}
	public void addRelations(List<Relation> relations){
		this.relations.addAll(relations);
	}
	
}
