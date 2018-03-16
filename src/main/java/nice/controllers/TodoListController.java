package nice.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.bytecode.Descriptor.Iterator;
import nice.exceptions.DataFormatException;
import nice.exceptions.ResourceNotFoundException;
import nice.models.TodoList;
import nice.services.TodoListService;
import nice.models.Task;

/*
 * Demonstrates how to set up RESTful API endpoints for /users
 */

@Controller
@EnableAutoConfiguration
@RequestMapping("/todolists")
@Api(tags = {"todolists"})
public class TodoListController extends AbstractRestHandler{

    @Autowired
    private TodoListService todoListService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "List all TodoLists", notes = "This endpoint is used to list all of the TodoLists.")
    public ResponseEntity<List<TodoList>> getAllTodoLists() {
        return ok(todoListService.findAll());
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Get a specific TodoList", notes = "This endpoint is used to get a specific TodoList.")
    public ResponseEntity<TodoList> getTodoListById(@ApiParam(value="Id of a TodoList", required=true) @PathVariable("id") Long id) {
    		TodoList todoList = todoListService.findById(id);
		checkResourceFound(todoList);//this check will make sure if the resource exists otherwise if will
		//return message and cause in 404
        return ok(todoList);
    }
    
    @RequestMapping(value = "/{id}/tasks",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Get a specific TodoList Tasks", notes = "This endpoint is used to get a specific TodoList Tasks.")
    public ResponseEntity<List<Task>> getTodoListTasksById(@ApiParam(value="Id of a TodoList", required=true) @PathVariable("id") Long id) {
    		TodoList todoList = todoListService.findById(id);
    		checkResourceFound(todoList);//this check will make sure if the resource exists otherwise if will
		//return message and cause in 404
		return ok(todoList.getTasks());
    	}

    @RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/json"},
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Create a TodoList", notes = "This endpoint is used to create a new TodoList.")
    public ResponseEntity<TodoList> createTodoList(@ApiParam(value="TodoList request object needed to create a new TodoList", required=true) @RequestBody TodoListRequest request, 
            HttpServletRequest requestHttp, HttpServletResponse responseHttp) {
        List<Task> tasks = getListOfTasks(request.getTasks());
        TodoList todoList = todoListService.createTodoList(request.getName(), tasks);
        //setting the response header so that we can run our test cases.
        responseHttp.setHeader("Location", requestHttp.getRequestURL().append("/").append(todoList.getId()).toString());
    		return ok(todoList);
    }
    
    

    @RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE,
			produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Delete a Todoist", notes = "This endpoint is used to delete a TodoList.")
    public void deleteTodoList(@ApiParam(value="Id of the TodoList", required=true) @PathVariable("id") Long id) {
    		if(id != null) {
    			TodoList todoList = todoListService.findById(id);
    			if(todoList != null) {
    				todoListService.deleteTodoList(id);
    			}
    			else {
    				throw new ResourceNotFoundException("No todoList id found to delete.");
    			}
    		}
		else {
			throw new DataFormatException("TodoList id for deleting a todoList cannot be null");
		}
    	}
    
    public List<Task> getListOfTasks(List<TaskRequest> taskRequests){
    		List<Task> tasks = new ArrayList<Task>();
    		if(taskRequests != null && taskRequests.size() > 0) {
    			for(TaskRequest taskReq:taskRequests) {
    				Task task = new Task();
    				task.setId(new Long(taskReq.getId()));
    				tasks.add(task);
    			}
    		}
    		return tasks;
    	}
}