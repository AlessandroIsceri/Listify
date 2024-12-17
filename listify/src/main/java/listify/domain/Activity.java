package listify.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Activity {
	private int id;
	private String name;
	private int priority = -1; //-1 = no priority
	private LocalDate expirationDate = null;
	private String category = "To Do";
	
	public Activity() {
		super();
	}
	
	public Activity(String name) {
		super();
		this.name = name;
	}
	
	public Activity(String name, int priority) {
		super();
		this.name = name;
		this.priority = priority;
	}

	public Activity(String name, int priority, String category) {
		super();
		this.name = name;
		this.priority = priority;
		this.category = category;
	}

	public Activity(String name, LocalDate expirationDate) {
		super();
		this.name = name;
		this.expirationDate = expirationDate;
	}

	public Activity(String name, int priority, LocalDate expirationDate) {
		super();
		this.name = name;
		this.priority = priority;
		this.expirationDate = expirationDate;
	}
	
	public Activity(String name, int priority, LocalDate expirationDate, String category) {
		super();
		this.name = name;
		this.priority = priority;
		this.expirationDate = expirationDate;
		this.category = category;
	}

	public Activity(int id, String name, int priority, LocalDate expirationDate, String category) {
		super();
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.expirationDate = expirationDate;
		this.category = category;
	}

	public Activity(int id, String name, int priority, String category) {
		super();
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.category = category;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public LocalDate getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", name=" + name + ", priority=" + priority + ", expirationDate=" + expirationDate
				+ ", category=" + category + "]";
	}
	
}

