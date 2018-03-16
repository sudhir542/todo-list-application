package nice.models;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Dao used to delegate CRUD operations 
 * against the data source here in this case its Task
 */

@Transactional
public interface TaskDao extends CrudRepository<Task, Long>,JpaSpecificationExecutor<Task> {

}