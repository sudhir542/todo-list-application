package nice.controllers;
/* 
 * Request Body to capture the request for Users 
 * 
 */

public class CreateUserRequest {
	
	private Long id;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}