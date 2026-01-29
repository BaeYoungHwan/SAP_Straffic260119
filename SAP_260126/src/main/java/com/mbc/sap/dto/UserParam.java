package com.mbc.sap.dto;

public class UserParam {
	private String category;
	private String keyword;
	private int pageNumber;
	
	public UserParam() {

	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public UserParam(String category, String keyword, int pageNumber) {
		super();
		this.category = category;
		this.keyword = keyword;
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		return "UserParam [category=" + category + ", keyword=" + keyword + ", pageNumber=" + pageNumber + "]";
	}
	
	

}
	
