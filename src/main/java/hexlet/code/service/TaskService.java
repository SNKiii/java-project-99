package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TaskService implements TaskServiceInterface {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;

    @Transactional(readOnly = true)
    public List<TaskDTO> getAll(TaskParamsDTO params) {
        Specification<Task> spec = taskSpecification.build(params);
        return taskRepository.findAll(spec)
                .stream()
                .map(taskMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskDTO getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task with id " + id + " not found"
                ));
        return taskMapper.map(task);
    }

    @Transactional
    public TaskDTO create(TaskCreateDTO createDTO) {
        Task task = taskMapper.map(createDTO);
        enrichTask(task, createDTO.getStatus(), createDTO.getAssigneeId(), createDTO.getTaskLabelIds());
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Transactional
    public TaskDTO update(Long id, TaskUpdateDTO updateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task with id " + id + " not found"
                ));
        taskMapper.update(updateDTO, task);
        enrichTaskFromNullable(task, updateDTO.getStatus(), updateDTO.getAssigneeId(), updateDTO.getTaskLabelIds());
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task with id " + id + " not found"
                ));
        taskRepository.delete(task);
    }

    private void enrichTask(Task task, String statusSlug, Long assigneeId, List<Long> labelIds) {
        if (statusSlug != null) {
            TaskStatus status = taskStatusRepository.findBySlug(statusSlug)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Status with slug " + statusSlug + " not found"));
            task.setTaskStatus(status);
        }

        if (assigneeId != null) {
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "User with id " + assigneeId + " not found"));
            task.setAssignee(assignee);
        }

        if (labelIds != null) {
            Set<Label> labels = new HashSet<>();
            for (Long id : labelIds) {
                Label label = labelRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Label with id " + id + " not found"));
                labels.add(label);
            }
            task.setLabels(labels);
        }
    }

    private void enrichTaskFromNullable(Task task, JsonNullable<String> statusSlugNullable,
                                        JsonNullable<Long> assigneeIdNullable,
                                        JsonNullable<List<Long>> labelIdsNullable) {
        String statusSlug = statusSlugNullable != null ? statusSlugNullable.orElse(null) : null;
        Long assigneeId = assigneeIdNullable != null ? assigneeIdNullable.orElse(null) : null;
        List<Long> labelIds = labelIdsNullable != null ? labelIdsNullable.orElse(null) : null;
        enrichTask(task, statusSlug, assigneeId, labelIds);
    }
}