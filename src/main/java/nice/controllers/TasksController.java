package nice.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nice.exceptions.DataFormatException;
import nice.exceptions.ResourceNotFoundException;
import nice.models.Task;
import nice.services.TaskService;

/*
 * Demonstrates how to set up RESTful API endpoints for /tasks
 */

@Controller
@EnableAutoConfiguration
@RequestMapping("/tasks")
public class TasksController extends AbstractRestHandler{
	@Autowired
    private TaskService taskService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Task>> getAllTasks() {
        return ok(taskService.findAll());
    }
    
    //Not need as per the requirement but still implemented.
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id) {
    		Task task = taskService.findById(id);
		checkResourceFound(task);//this check will make sure if the resource exists otherwise if will
		//return message and cause in 404
        return ok(task);
    }

    @RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/json"},
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        return ok(taskService.createTask(request.getName(), request.getDesc(), request.getStatus(), request.getUser().getId()));
    }

    //Not need as per the requirement but still implemented.
    @RequestMapping(value = "/{id}",
			method = RequestMethod.PUT,
			consumes = {"application/json"},
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @RequestBody TaskRequest request) {
    		if (id != null) {
    			Task task = taskService.findById(id);
    			checkResourceFound(task);//this check will make sure if the resource exists otherwise if will
    			//return message and cause in 404
    			return ok(taskService.updateTask(id, request.getName(), request.getDesc(), request.getStatus(), request.getUser().getId()));
    		}
    		else 
    			throw new DataFormatException("User id for updating a user name cannot be null");
    }
    
    @RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE,
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteTask(@PathVariable("id") Long id) {
    		if(id != null) {
    			Task task = taskService.findById(id);
    			if(task != null) {
    				taskService.deleteTask(id);
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
