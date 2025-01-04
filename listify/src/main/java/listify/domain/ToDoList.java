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
		toDoList = new ArrayList<Activity>();
	}

	public ToDoList(int id, String name) {
		this.id = id;
		this.name = name;
		toDoList = new ArrayList<Activity>();
	}

	//methods to add/remove an activity from the to-do list
	public void addActivity(Activity activity) {
		toDoList.add(activity);
	}
	
	public void removeActivity(Activity activity) {
		toDoList.remove(activity);
	}
	
	public Activity getActivity(int activityId) {
		for(Activity activity: toDoList) {
			if(activityId == activity.getId()) {
				return activity;
			}
		}
		return null;
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
