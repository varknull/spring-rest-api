package com.rest.resource;

import org.springframework.data.domain.Page;

public class Response {

	private int totalCount;
	private Page<Alumn> data;
	
	
	public Response() {}
	
	public Response(int totalCount, Page<Alumn> data) {
		this.totalCount = totalCount;
		this.data = data;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public Page<Alumn> getData() {
		return data;
	}
	public void setData(Page<Alumn> data) {
		this.data = data;
		this.totalCount = data.getNumberOfElements();
	}

	
}
