package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;

import java.util.List;

public interface TaskStatusServiceInterface {
    List<TaskStatusDTO> getAll();
    TaskStatusDTO getById(Long id);
    TaskStatusDTO getBySlug(String slug);
    TaskStatusDTO create(TaskStatusCreateDTO createDTO);
    TaskStatusDTO update(Long id, TaskStatusUpdateDTO updateDTO);
    void delete(Long id);
}