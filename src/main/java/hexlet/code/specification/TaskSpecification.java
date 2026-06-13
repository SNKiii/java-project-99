package hexlet.code.specification;

import hexlet.code.model.Task;
import hexlet.code.model.Label;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public static Specification<Task> titleContains(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> assigneeIdEquals(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> statusEquals(String statusSlug) {
        if (statusSlug == null || statusSlug.isEmpty()) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("taskStatus").get("slug"), statusSlug);
    }

    public static Specification<Task> labelIdEquals(Long labelId) {
        if (labelId == null) {
            return null;
        }
        return (root, query, cb) -> {
            var labelsJoin = root.join("labels");
            return cb.equal(labelsJoin.get("id"), labelId);
        };
    }
}
