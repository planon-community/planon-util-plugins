package edu.planon.lib.client.common.dto;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

public class PnQueryParamDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final long page;
	private final long pageSize;
	private final SortParam<String> sortParam;
	
	public PnQueryParamDTO(long page, long pageSize, SortParam<String> sortParam) {
		this.page = page;
		this.pageSize = pageSize;
		this.sortParam = sortParam;
	}
	
	public PnQueryParamDTO(long page, long pageSize, String sortProperty, boolean isAscending) {
		this(page, pageSize, new SortParam<String>(sortProperty, isAscending));
	}
	
	public long getPage() {
		return this.page;
	}
	
	public long getPageSize() {
		return this.pageSize;
	}
	
	public SortParam<String> getSort() {
		return this.sortParam;
	}
	
	public String getProperty() {
		return this.sortParam.getProperty();
	}
	
	public boolean isAscending() {
		return this.sortParam.isAscending();
	}
}
