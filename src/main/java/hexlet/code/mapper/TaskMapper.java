package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TaskMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    public Task toEntity(TaskCreateDTO dto) {
        if (dto == null) return null;
        Task task = new Task();
        task.setIndex(dto.getIndex());
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());

        if (dto.getAssigneeId() != null) {
            userRepository.findById(dto.getAssigneeId()).ifPresent(task::setAssignee);
        }
        if (dto.getStatus() != null) {
            taskStatusRepository.findBySlug(dto.getStatus()).ifPresent(task::setTaskStatus);
        }
        if (dto.getLabelIds() != null) {
            task.setLabels(dto.getLabelIds().stream()
                    .map(labelRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toList()));
        }

        return task;
    }

    public TaskDTO toDto(Task task) {
        if (task == null) return null;
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setIndex(task.getIndex());
        dto.setTitle(task.getTitle());
        dto.setContent(task.getContent());
        dto.setCreatedAt(task.getCreatedAt());

        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        if (task.getTaskStatus() != null) {
            dto.setStatus(task.getTaskStatus().getSlug());
        }
        if (task.getLabels() != null) {
            dto.setLabelIds(task.getLabels().stream()
                    .map(Label::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public void updateEntity(TaskUpdateDTO dto, Task task) {
        if (dto == null || task == null) return;

        if (dto.getIndex() != null && dto.getIndex().isPresent()) {
            task.setIndex(dto.getIndex().get());
        }
        if (dto.getTitle() != null && dto.getTitle().isPresent()) {
            task.setTitle(dto.getTitle().get());
        }
        if (dto.getContent() != null && dto.getContent().isPresent()) {
            task.setContent(dto.getContent().get());
        }
        if (dto.getAssigneeId() != null && dto.getAssigneeId().isPresent()) {
            userRepository.findById(dto.getAssigneeId().get()).ifPresent(task::setAssignee);
        }
        if (dto.getStatus() != null && dto.getStatus().isPresent()) {
            taskStatusRepository.findBySlug(dto.getStatus().get()).ifPresent(task::setTaskStatus);
        }
        if (dto.getLabelIds() != null && dto.getLabelIds().isPresent()) {
            task.setLabels(dto.getLabelIds().get().stream()
                    .map(labelRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toList()));
        }
    }
}
