package com.app.base.data;

import java.io.Serializable;

public class Paginator implements Serializable {
	private static final long serialVersionUID = -7245282569244687012L;

	public static ThreadLocal<Integer> page = new ThreadLocal<Integer>();
	public static ThreadLocal<Integer> rows = new ThreadLocal<Integer>();

	public void setPage(Integer page) {
		if(null==page) {
			page = 1;
		}
		Paginator.page.set(page);
	}
	public int getPage() {
		return Paginator.page.get();
	}
	public void setRows(Integer rows) {
		if(null==rows) {
			rows = 10000;
		}
		Paginator.rows.set(rows);
	}
	public int getRows() {
		return Paginator.rows.get();
	}

}
