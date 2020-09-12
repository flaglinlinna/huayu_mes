package com.app.base.data;

import java.io.Serializable;
import java.util.List;

public class DataGrid implements Serializable {
    private static final long serialVersionUID = -8747473618280525378L;

    /**
     * 返回数据
     */
    protected List rows;
    /**
     * 页码
     */
    protected int page;
    /**
     * 每页大小
     */
    protected int pageSize;
    /**
     * 总数
     */
    protected int total;

    public DataGrid() {
    }

    public DataGrid(List rows, int total, int page, int pageSize) {
        this.rows = rows;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public DataGrid(List rows, int total) {
        this.rows = rows;
        this.total = total;
    }

    public static DataGrid create() {
        return new DataGrid();
    }

    public static DataGrid create(List rows, int total) {
        return create(rows, total, 0, 0);
    }

    public static DataGrid create(List rows, int total, int page, int pageSize) {
        return new DataGrid(rows, total, page, pageSize);
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
