package com.app.query.dao;

public class Parameter {

	private String name;
	private String operator;
	private Object value;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Parameter() {
	}

	public Parameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public Parameter(String name, String operator, Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public static Parameter build(String name, Object value) {
		return new Parameter(name, value);
	}

	public static Parameter build(String name, String operator, Object value) {
		return new Parameter(name, operator, value);
	}
}
