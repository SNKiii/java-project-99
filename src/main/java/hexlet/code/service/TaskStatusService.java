package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll()
                .stream()
                .map(taskStatusMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskStatusDTO getById(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
        return taskStatusMapper.toDto(taskStatus);
    }

    @Transactional(readOnly = true)
    public TaskStatusDTO getBySlug(String slug) {
        TaskStatus taskStatus = taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with slug " + slug + " not found"));
        return taskStatusMapper.toDto(taskStatus);
    }

    @Transactional
    public TaskStatusDTO create(TaskStatusCreateDTO createDTO) {
        if (taskStatusRepository.existsBySlug(createDTO.getSlug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Task status with slug '" + createDTO.getSlug() + "' already exists");
        }
        if (taskStatusRepository.existsByName(createDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Task status with name '" + createDTO.getName() + "' already exists");
        }

        TaskStatus taskStatus = taskStatusMapper.toEntity(createDTO);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(taskStatus);
    }

    @Transactional
    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO updateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));

        if (updateDTO.getName() != null && updateDTO.getName().isPresent()) {
            String newName = updateDTO.getName().get();
            if (!newName.equals(taskStatus.getName()) && taskStatusRepository.existsByName(newName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Task status with name '" + newName + "' already exists");
            }
        }

        if (updateDTO.getSlug() != null && updateDTO.getSlug().isPresent()) {
            String newSlug = updateDTO.getSlug().get();
            if (!newSlug.equals(taskStatus.getSlug()) && taskStatusRepository.existsBySlug(newSlug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Task status with slug '" + newSlug + "' already exists");
            }
        }

        taskStatusMapper.updateEntity(updateDTO, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(taskStatus);
    }

    @Transactional
    public void delete(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));

        if (taskRepository.existsByTaskStatus(taskStatus)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete status with existing tasks");
        }

        taskStatusRepository.deleteById(id);
    }
}
