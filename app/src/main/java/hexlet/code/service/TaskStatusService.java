package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll()
                .stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    public TaskStatusDTO getById(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO getBySlug(String slug) {
        TaskStatus taskStatus = taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with slug " + slug + " not found"));
        return taskStatusMapper.map(taskStatus);
    }

    @Transactional
    public TaskStatusDTO create(TaskStatusCreateDTO createDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(createDTO);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    @Transactional
    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO updateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));

        if (updateDTO.getName() != null && updateDTO.getName().isPresent()) {
            taskStatus.setName(updateDTO.getName().get());
        }
        if (updateDTO.getSlug() != null && updateDTO.getSlug().isPresent()) {
            taskStatus.setSlug(updateDTO.getSlug().get());
        }

        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    @Transactional
    public void delete(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
