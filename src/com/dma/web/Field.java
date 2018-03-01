package com.dma.web;

public class Field {

	String _id = "";
	String _ref = null;
	String field_name = "";
	String field_type = "";
	boolean pk = false;
	boolean index = false;
	String label = "";
	int field_size = 0;
	String nullable = "NO";
	boolean traduction = false;
	boolean visible = false;
	boolean timezone = false;
	String icon = "Attribute";
	String displayType = "Value";
	String description = "";
	
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
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getField_type() {
		return field_type;
	}
	public void setField_type(String field_type) {
		this.field_type = field_type;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pk) {
		this.pk = pk;
	}
	public boolean isIndex() {
		return index;
	}
	public void setIndex(boolean index) {
		this.index = index;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getField_size() {
		return field_size;
	}
	public void setField_size(int field_size) {
		this.field_size = field_size;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public boolean isTraduction() {
		return traduction;
	}
	public void setTraduction(boolean traduction) {
		this.traduction = traduction;
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isTimezone() {
		return timezone;
	}
	public void setTimezone(boolean timezone) {
		this.timezone = timezone;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
