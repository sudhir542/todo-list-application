package nice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import nice.models.TodoListDao;
import nice.models.TodoList;
import nice.models.Task;

/*
 * Service that helps TodoApplication to do its thing
 */

@Service
public class TodoListService {

	@Autowired
    private TodoListDao todoListDao;

	@Transactional
    public List<TodoList> findAll() {
		return (List<TodoList>) todoListDao.findAll();
    }
	
	 @Transactional
	public TodoList findById(Long id) {
		TodoList todoList = todoListDao.findOne(id);
        try {
            todoList = todoListDao.findOne(id);
        } catch (Exception e) {
            todoList = null;
        }

        return todoList;
	}
	

    @Transactional
	public TodoList createTodoList(String name, List<Task> tasks) {
		if(tasks != null && tasks.size() > 0) {
    			TodoList todoList = new TodoList(name,tasks);
    			return todoListDao.save(todoList);
    		}
		else {
			return null;
		}
	}
    
    //write the logic for grouping todolist

	@Transactional
	public void deleteTodoList(Long id) {
		try {
			TodoList todoList = todoListDao.findOne(id);
			if(todoList != null) {
				todoListDao.delete(id);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
}