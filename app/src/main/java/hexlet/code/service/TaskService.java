package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskDTO> getAll(TaskParamsDTO params) {
        Specification<Task> spec = Specification
                .where(TaskSpecification.titleContains(params.getTitleCont()))
                .and(TaskSpecification.assigneeIdEquals(params.getAssigneeId()))
                .and(TaskSpecification.statusEquals(params.getStatus()))
                .and(TaskSpecification.labelIdEquals(params.getLabelId()));

        return taskRepository.findAll(spec)
                .stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    @Transactional
    public TaskDTO create(TaskCreateDTO createDTO) {
        Task task = taskMapper.map(createDTO);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Transactional
    public TaskDTO update(Long id, TaskUpdateDTO updateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(updateDTO, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}