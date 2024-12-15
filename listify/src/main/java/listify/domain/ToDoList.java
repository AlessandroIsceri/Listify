package listify.domain;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
	private List<Activity> toDoList;
	
	public ToDoList() {
		super();
		this.toDoList = new ArrayList<Activity>();
	}

	private boolean addItem(Activity item) {
		return toDoList.add(item);
	}
	
	private boolean removeItem(Activity item) {
		for(int i = 0; i < toDoList.size(); i++){
			if(toDoList.get(i).equals(item)) {
				toDoList.remove(i);
				return true;
			}
		}
		return false;
	}
	
}
