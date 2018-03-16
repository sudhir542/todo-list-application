package nice.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

/**
 * Dao used to delegate CRUD operations 
 * against the data source here in this case its TodoList
 */

@Transactional
public interface TodoListDao extends CrudRepository<TodoList, Long> {

}