package nice.specification;

import nice.models.Task;
import org.springframework.data.jpa.domain.Specification;
import nice.util.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/*
 * This OrderSpecification helps to implement Search Criteria in Tasks
 */

public class TaskSpecification implements Specification<Task> {
    private SearchCriteria searchCriteria;

    /*
     * Constructor for OrderSpecification
     */
    public TaskSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

   /*
    * Override the toPredicate and implement the logic for search here.
    */
    
    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThan(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }
        if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThan(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }
        if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(root.<String>get(searchCriteria.getKey()), "%" + searchCriteria.getValue().toString() + "%");
            } else {
                return criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            }
        }
        return null;
    }
}