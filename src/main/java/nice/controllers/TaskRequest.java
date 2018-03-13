package nice.controllers;

import nice.controllers.CreateUserRequest;

public class TaskRequest {
	private String name;
	private String desc;
	private String status;
	private CreateUserRequest user;//user will be used to send used.id during PUT,POST 
	
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
