package hexlet.code.controller;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@AllArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> index() {
        List<TaskStatusDTO> taskStatuses = taskStatusService.getAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(taskStatuses.size()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(taskStatuses);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO createDTO) {
        return taskStatusService.create(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO update(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateDTO updateDTO) {
        return taskStatusService.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}