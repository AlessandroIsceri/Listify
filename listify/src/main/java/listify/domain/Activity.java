package listify.domain;

import java.time.LocalDate;

public class Activity {
	private int id;
	private String name;
	private int priority = -1; //-1 = no priority
	private LocalDate expirationDate = null;
	private String category = "To Do";

	public Activity() {
		super();
	}
	
	public Activity(int id, String name, int priority, LocalDate expirationDate, String category) {
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.expirationDate = expirationDate;
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

