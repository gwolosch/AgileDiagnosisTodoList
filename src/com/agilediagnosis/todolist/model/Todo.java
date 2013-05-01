package com.agilediagnosis.todolist.model;

public class Todo {
	
	private int pk;
	private String title;
	private boolean completed;
	
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public Todo(int pk,
				String title,
				boolean completed) {
		this.pk = pk;
		this.title = title;
		this.completed = completed;
	}
	
	public String toString() {
		return "{"+ pk + ": " + title + ", " + ((completed) ? "completed"
												 			: "!completed") + "}";
	}
}
