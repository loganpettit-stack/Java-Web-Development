package edu.umsl.math.beans;

import java.util.List;

public class Problem {
	
	private int pid;
	private String content;
	private int order_num;
	private List<String> categoryList;
	private List<Category> availableCategories;
//	private List<String> keywordList;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getOrder_num() {
		return order_num;
	}
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}
//	public void setKeywordList(List<String> keywordList) {
//		this.keywordList = keywordList;
//	}
	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}
	public void setAvailableCategories(List<Category> availableCategories) {
		this.availableCategories = availableCategories;
	}
	
//	public String getKeywordList() {
//		StringBuilder sb = new StringBuilder();
//		
//		if(keywordList.size() >= 1) {
//			for(int i = 0; i < keywordList.size(); i++) {
//				sb.append(keywordList.get(i) + "\n");
//			}
//		}
//		else {
//			sb.append("No associated keywords");
//		}
//		return sb.toString();
//	}
	
	public String getCategoryList() {
		StringBuilder sb = new StringBuilder();
		
		if(categoryList.size() >= 1) {
			for(int i = 0; i < categoryList.size(); i++) {
			sb.append(categoryList.get(i) + "\n");
			}
		}
		else {
			sb.append("No category");
		}
		return sb.toString();
	}
	
	public List<Category> getAvailableCategories() {
		return availableCategories;
	}
	
	
	

	
}
