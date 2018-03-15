package nice.models;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import nice.models.Task;

@Entity
@Table(name = "todolists")
public class TodoList {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Column(nullable = false, length = 30)
    private String name;
    
    protected TodoList() {}

    public TodoList(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }
    
    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "todolist_task",
            joinColumns = @JoinColumn(name = "todolist_id", updatable = false, nullable = false),
            inverseJoinColumns = @JoinColumn(name = "task_id", updatable = false, nullable = false)
    )
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public String toString() {
        return String.format(
                "TodoList[id=%d, name='%s']",
                id, name);
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoList todo = (TodoList) o;

        if (id != todo.id) return false;
        return !(name != null ? !name.equals(todo.name) : todo.name != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    
}

