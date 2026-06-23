package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;

import java.util.List;

public interface TaskServiceInterface {
    List<TaskDTO> getAll(TaskParamsDTO params);
    TaskDTO getById(Long id);
    TaskDTO create(TaskCreateDTO createDTO);
    TaskDTO update(Long id, TaskUpdateDTO updateDTO);
    void delete(Long id);
}