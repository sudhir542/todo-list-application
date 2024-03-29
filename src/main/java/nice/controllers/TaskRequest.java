package nice.controllers;

import nice.controllers.CreateUserRequest;

/* 
 * Request Body to capture the request for Task 
 * 
 */

public class TaskRequest {
	private String id;
	private String name;
	private String desc;
	private String status;
	private CreateUserRequest user;//user will be used to send used.id during PUT,POST 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setUser(CreateUserRequest user) {
		this.user = user;
	}
	
	public CreateUserRequest getUser() {
		return user;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
