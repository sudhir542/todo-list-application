package nice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nice.models.Task;
import nice.models.TaskDao;
import nice.models.User;
import nice.models.UserDao;

@Service
public class TaskService {

	@Autowired
    private TaskDao taskDao;
	
	@Autowired
	private UserDao userDao;

	@Transactional
    public List<Task> findAll() {
    	return (List<Task>) taskDao.findAll();
    }

    @Transactional
	public Task findById(Long id) {
		Task task;

        try {
            task = taskDao.findOne(id);
        } catch (Exception e) {
            task = null;
        }

        return task;
	}

	@Transactional
	public Task createTask(String name, String desc, String status, Long userId) {
		User user = userDao.findOne(userId);
		if(user != null) {
			Task task = new Task(name, desc, status, user);
			return taskDao.save(task);
		}
		else {
			return null;
		}
    }

	@Transactional
	public Task updateTask(Long id, String name, String desc, String status, Long userId) {
		try {
            Task task = taskDao.findOne(id);
            if(name != null && !name.isEmpty()) {
            		task.setName(name);
            }
            if(desc != null && !desc.isEmpty()) {
            		task.setDesc(desc);
            }
            if(status != null && !status.isEmpty()) {
            		if(status.equalsIgnoreCase("not started") || status.equalsIgnoreCase("in progress") || status.equalsIgnoreCase("completed"))
            		task.setStatus(status);
            }
            if(userId != null) {
            		User user = userDao.findOne(userId);
        			if(user != null) {
        				task.setUser(user);
        			}
        			else {
        				return null;
        			}
            }
            return taskDao.save(task);
        } catch (Exception e) {
            return null;
        }
	}
	
	@Transactional
	public void deleteTask(Long id) {
		try {
			Task task = taskDao.findOne(id);
			if(task != null) {
				taskDao.delete(id);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
}
