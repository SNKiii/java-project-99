package hexlet.code.repository;

import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @EntityGraph(attributePaths = {"taskStatus", "assignee", "labels"})
    List<Task> findAll();

    @EntityGraph(attributePaths = {"taskStatus", "assignee", "labels"})
    List<Task> findAll(org.springframework.data.jpa.domain.Specification<Task> spec);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Task t WHERE t.assignee = :user")
    boolean existsByAssignee(@Param("user") User user);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Task t WHERE t.taskStatus = :status")
    boolean existsByTaskStatus(@Param("status") TaskStatus status);
}