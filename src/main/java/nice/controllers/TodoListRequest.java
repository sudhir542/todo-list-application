package nice.controllers;

import nice.controllers.TaskRequest;
import java.util.List;

/* 
 * Request Body to capture the request for TodoList 
 * 
 */

public class TodoListRequest {
	private String name;
	private List<TaskRequest> tasks;//task id can be a part of the todo List
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TaskRequest> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskRequest> tasks) {
		this.tasks = tasks;
	}

}
