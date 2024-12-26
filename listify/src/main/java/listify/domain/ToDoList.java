package listify.domain;

import java.util.List;

public class ToDoList {
	private int id;
	private String name;
	private List<Activity> toDoList;
	
	public ToDoList() {
		super();
	}

	public ToDoList(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public boolean addItem(Activity item) {
		return toDoList.add(item);
	}
	
	public boolean removeItem(Activity item) {
		for(int i = 0; i < toDoList.size(); i++){
			if(toDoList.get(i).equals(item)) {
				toDoList.remove(i);
				return true;
			}
		}
		return false;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Activity> getToDoList() {
		return toDoList;
	}
	
	public void setToDoList(List<Activity> toDoList) {
		this.toDoList = toDoList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
