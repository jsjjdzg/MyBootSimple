package com.bocloud.emaas.common.model;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 当前页数据信息类
 * 
 * @author dmw
 *
 */
public class GridBean {

	private int page; // 当前页
	private int pages;// 总页数
	private int total; // 总条数
	private List<?> rows;// 当前页数据

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the rows
	 */
	public List<?> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	/**
	 * @param page
	 * @param pages
	 * @param total
	 * @param rows
	 */
	public GridBean(int page, int pages, int total, List<?> rows) {
		super();
		this.page = page;
		this.pages = pages;
		this.total = total;
		this.rows = rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}