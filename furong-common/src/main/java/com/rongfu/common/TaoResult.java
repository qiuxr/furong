package com.rongfu.common;

import java.io.Serializable;
import java.util.List;

public class TaoResult<T> implements Serializable {
	private static final long serialVersionUID = 8591468069988883824L;
	private Long total;
	private List<T> rows;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	
}
