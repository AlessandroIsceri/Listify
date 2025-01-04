package listify.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Activity {
	private int id;
	private String name;
	private int priority = -1; //-1 = no priority
	private LocalDate expirationDate = null;
	private String category = "To Do";

	//constructor for Jackson
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

	// getters and setters
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

	@Override
	public int hashCode() {
		return Objects.hash(category, expirationDate, id, name, priority);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		return Objects.equals(category, other.category) && Objects.equals(expirationDate, other.expirationDate)
				&& id == other.id && Objects.equals(name, other.name) && priority == other.priority;
	}

}

