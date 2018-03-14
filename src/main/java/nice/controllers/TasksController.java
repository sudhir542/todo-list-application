package nice.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nice.util.SearchCriteria;
import nice.specification.TaskSpecification;

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
    				checkResourceFound(task);
    			}
    		}
		else {
			throw new DataFormatException("User id for deleting a user cannot be null");
		}
    	}
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    /*
     * Search based on request params.
     * This method is used to search on orders with parameters like status, products, discount etc. ")
     */
    public ResponseEntity<List<Task>> search(@RequestParam(value = "search", defaultValue = "") String search) {
        if (search == null || search.trim().isEmpty()) {
            return ok(taskService.findAll());
        }
        SearchCriteria searchCriteria = null;
        Pattern pattern = Pattern.compile("(\\w+)(:|<|>)(\\w+)");
        Matcher matcher = pattern.matcher(search);
        while (matcher.find()) {
            if (matcher.group(1).equalsIgnoreCase("userId")) {
            		//do not use dynamic ajax calling this will lot of tasks to called
            		//since only a limited number of tasks would exist this would be good approach
            	    List<Task> taskResults = getUsersForTaskCriteria(taskService.findAll(), matcher.group(3));
                return ok(taskResults);
            }
            searchCriteria = new SearchCriteria(matcher.group(1), matcher.group(2), toTitleCase(matcher.group(1), matcher.group(3)));
        }
        if (searchCriteria == null) {
        		throw new ResourceNotFoundException("Resource not found.");
        }

        TaskSpecification taskSpecification = new TaskSpecification(searchCriteria);
        Specifications<Task> result = Specifications.where(taskSpecification);

        List<Task> taskResults = taskService.listTaskBySpecification(result);

        if (taskResults == null || taskResults.isEmpty()) {
        		checkResourceFound(taskResults);
        }

        return ok(taskResults);
    }
    
    /**
     * get tasks for user criteria
     * @param Tasks
     * @param value
     * @return
     */
    private List<Task> getUsersForTaskCriteria(List<Task> tasks, String value) {
        List<Task> resultTask = new ArrayList<Task>();
        Long userId = new Long(value);
        for (Task task : tasks) {
        		if(task.getUser().getId() == userId) {
        			resultTask.add(task);
        		}
        }
        return resultTask;
    }
    
    private String toTitleCase(String testCase, String input) {
    		if(testCase.equalsIgnoreCase("status")) {
	        String[] arr = input.split(" ");
	        StringBuffer sb = new StringBuffer();
	
	        for (int i = 0; i < arr.length; i++) {
	            sb.append(Character.toUpperCase(arr[i].charAt(0)))
	                .append(arr[i].substring(1)).append(" ");
	        }          
	        return sb.toString().trim();
    		}
    		else
    			return input;
    }  

}
