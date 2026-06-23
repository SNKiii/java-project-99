package hexlet.code.specification;

import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        if (params == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        Specification<Task> spec = Specification.where(titleContains(params.getTitleCont()));
        spec = spec.and(assigneeIdEquals(params.getAssigneeId()));
        spec = spec.and(statusEquals(params.getStatus()));
        spec = spec.and(labelIdEquals(params.getLabelId()));
        return spec;
    }

    public static Specification<Task> titleContains(String title) {
        if (title == null || title.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> assigneeIdEquals(Long assigneeId) {
        if (assigneeId == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) ->
                cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> statusEquals(String statusSlug) {
        if (statusSlug == null || statusSlug.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) ->
                cb.equal(root.get("taskStatus").get("slug"), statusSlug);
    }

    public static Specification<Task> labelIdEquals(Long labelId) {
        if (labelId == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            query.distinct(true);
            var labelsJoin = root.join("labels");
            return cb.equal(labelsJoin.get("id"), labelId);
        };
    }
}



