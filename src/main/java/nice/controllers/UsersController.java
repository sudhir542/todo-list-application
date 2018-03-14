package nice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nice.exceptions.ResourceNotFoundException;
import nice.exceptions.DataFormatException;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import nice.models.User;
import nice.services.UserService;

/*
 * Demonstrates how to set up RESTful API endpoints for /users
 */

@Controller
@EnableAutoConfiguration
@RequestMapping("/users")
@Api(tags = {"users"})
public class UsersController extends AbstractRestHandler{

    @Autowired
    private UserService userService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "List all users", notes = "This endpoint is used to list all of the users.")
    public ResponseEntity<List<User>> getAllUsers() {
        return ok(userService.findAll());
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Get a specific user", notes = "This endpoint is used to get a specific user.")
    public ResponseEntity<User> getUserByIdOrUserName(@ApiParam(value="Id or Username required to update", required=true) @PathVariable("id") String idOrUserName) {
    		User user = userService.findByIdOrUserName(idOrUserName);
		checkResourceFound(user);//this check will make sure if the resource exists otherwise if will
		//return message and cause in 404
        return ok(user);
    }

    @RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/json"},
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Create a user", notes = "This endpoint is used to create a new user.")
    public ResponseEntity<User> createUser(@ApiParam(value="User request object needed to create a new user mostly username", required=true) @RequestBody CreateUserRequest request) {
        return ok(userService.createUser(request.getUserName()));
    }

    @RequestMapping(value = "/{id}",
			method = RequestMethod.PUT,
			consumes = {"application/json"},
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Update a user", notes = "This endpoint is used to update a user.")
    public ResponseEntity<User> updateUser(@ApiParam(value="User request needed to update user only username", required=false) @PathVariable("id") Long id, @RequestBody CreateUserRequest request) {
    		if (id != null) {
    			User user = userService.findByIdOrUserName(id.toString());
    			checkResourceFound(user);//this check will make sure if the resource exists otherwise if will
    			//return message and cause in 404
    			return ok(userService.updateUser(id, request.getUserName()));
    		}
    		else 
    			throw new DataFormatException("User id for updating a user name cannot be null");
    }
    
    @RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE,
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Delete a user", notes = "This endpoint is used to delete a user.")
    public void deleteUser(@ApiParam(value="Id of the user", required=true) @PathVariable("id") Long id) {
    		if(id != null) {
    			User user = userService.findByIdOrUserName(id.toString());
    			if(user != null) {
    				userService.deleteUser(id);
    			}
    			else {
    				throw new ResourceNotFoundException("No user id found to delete.");
    			}
    		}
		else {
			throw new DataFormatException("User id for deleting a user cannot be null");
		}
    	}
}