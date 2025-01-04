package listify.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToDoList {
	private int id;
	private String name;
	private List<Activity> toDoList;
	
	//constructor for Jackson
	public ToDoList() {
		super();
	}

	public ToDoList(int id, String name) {
		this.id = id;
		this.name = name;
		toDoList = new ArrayList<Activity>();
	}

	//methods to add/remove an activity from the to-do list
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

	//getters and setters
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

	@Override
	public String toString() {
		return "ToDoList [id=" + id + ", name=" + name + ", toDoList=" + toDoList.toString() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, toDoList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToDoList other = (ToDoList) obj;
		return id == other.id && Objects.equals(name, other.name) && Objects.equals(toDoList, other.toDoList);
	}
	
}
