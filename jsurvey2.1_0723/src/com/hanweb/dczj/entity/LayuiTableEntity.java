package com.hanweb.dczj.entity;

public class LayuiTableEntity {

	private String field;
	
	private String title;
	
	private String align;
	
	private String style;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public LayuiTableEntity(String field, String title, String align, String style) {
		super();
		this.field = field;
		this.title = title;
		this.align = align;
		this.style = style;
	}

	public LayuiTableEntity() {
		super();
	}
	
}
