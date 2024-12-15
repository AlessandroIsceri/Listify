package listify.domain;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
	private String name;
	private List<Activity> toDoList;
	
	public ToDoList(String name) {
		super();
		this.name = name;
		this.toDoList = new ArrayList<Activity>();
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

	public String getName() {
		return name;
	}

	public List<Activity> getToDoList() {
		return toDoList;
	}
	
}
