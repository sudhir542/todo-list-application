package nice.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TaskDao extends CrudRepository<Task, Long> {
  
}