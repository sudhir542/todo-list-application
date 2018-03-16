package nice.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = {"tasks"})
public class TasksController extends AbstractRestHandler{
	
	@Autowired
    private TaskService taskService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "List all tasks and also search on task parameters", notes = "This endpoint is used to list all of the tasks "
    		+ "http://localhost:8080/tasks and one can search based on task parameters too like " 
    		+ "for example " 
    		+ "http://localhost:8080/tasks?search=status:completed or ?search=userId:1")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ok(taskService.findAll());
    }
    
    //Not need as per the requirement but still implemented.
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Get a task details", notes = "This endpoint is used to get information about specific task.")
    public ResponseEntity<Task> getTaskById(@ApiParam(value="Id of the task", required=true) @PathVariable("id") Long id) {
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
    @ApiOperation(value = "Create a task", notes = "This endpoint is used to create a new task. Desc is an optional field.")
    public ResponseEntity<Task> createTask(@ApiParam(value="Request body with name, desc(optional), status, user", required=true) @RequestBody TaskRequest request, 
            HttpServletRequest requestHttp, HttpServletResponse responseHttp) {
    		Task task = taskService.createTask(request.getName(), request.getDesc(), request.getStatus(), request.getUser().getId());
    		//setting the response header so that we can run our test cases.
    		responseHttp.setHeader("Location", requestHttp.getRequestURL().append("/").append(task.getId()).toString());
        return ok(task);
    }

    //Not need as per the requirement but still implemented.
    @RequestMapping(value = "/{id}",
			method = RequestMethod.PUT,
			consumes = {"application/json"},
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Update a task", notes = "This endpoint is used to update an existing task. All the parameters are optional.")
    public ResponseEntity<Task> updateTask(@ApiParam(value="Id of the task", required=true) @PathVariable("id") Long id, @ApiParam(value="Request body with name, desc(optional), status, user", required=false) @RequestBody TaskRequest request) {
    		if (id != null) {
    			Task task = taskService.findById(id);
    			checkResourceFound(task);//this check will make sure if the resource exists otherwise if will
    			//return message and cause in 404
    			return ok(taskService.updateTask(id, request.getName(), request.getDesc(), request.getStatus(), request.getUser().getId()));
    		}
    		else 
    			throw new DataFormatException("Task id for updating a task information cannot be null.");
    }
    
    @RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE,
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Delete a task", notes = "This endpoint is used to delete an existing task.")
    public void deleteTask(@ApiParam(value="Id of the task", required=true) @PathVariable("id") Long id) {
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
			throw new DataFormatException("Task id for deleting a task cannot be null.");
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
