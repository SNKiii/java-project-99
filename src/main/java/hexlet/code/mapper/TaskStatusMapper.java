package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusMapper {

    public TaskStatus toEntity(TaskStatusCreateDTO dto) {
        if (dto == null) return null;
        TaskStatus status = new TaskStatus();
        status.setName(dto.getName());
        status.setSlug(dto.getSlug());
        return status;
    }

    public TaskStatusDTO toDto(TaskStatus status) {
        if (status == null) return null;
        TaskStatusDTO dto = new TaskStatusDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setSlug(status.getSlug());
        dto.setCreatedAt(status.getCreatedAt());
        return dto;
    }

    public void updateEntity(TaskStatusUpdateDTO dto, TaskStatus status) {
        if (dto == null || status == null) return;
        if (dto.getName() != null && dto.getName().isPresent()) {
            status.setName(dto.getName().get());
        }
        if (dto.getSlug() != null && dto.getSlug().isPresent()) {
            status.setSlug(dto.getSlug().get());
        }
    }
}
