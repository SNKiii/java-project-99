package hexlet.code.repository;

import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    Optional<TaskStatus> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Task t WHERE t.taskStatus = :status")
    boolean existsByTaskStatus(@Param("status") TaskStatus status);
}

