package listify.domain;

import java.util.Date;

public class Activity {
	private String name;
	private int priority = -1; //-1 = no priority
	private Date expirationDate = null;
	
	
	public Activity(String name) {
		super();
		this.name = name;
	}
	
	public Activity(String name, int priority) {
		super();
		this.name = name;
		this.priority = priority;
	}

	public Activity(String name, Date expirationDate) {
		super();
		this.name = name;
		this.expirationDate = expirationDate;
	}

	public Activity(String name, int priority, Date expirationDate) {
		super();
		this.name = name;
		this.priority = priority;
		this.expirationDate = expirationDate;
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
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}

