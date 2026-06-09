package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Label l JOIN l.tasks t WHERE l.id = :labelId")
    boolean existsByTasks(@Param("labelId") Long labelId);
}