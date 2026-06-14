package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Transactional(readOnly = true)
    public List<LabelDTO> getAll() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public LabelDTO getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.toDto(label);
    }

    @Transactional(readOnly = true)
    public LabelDTO getByName(String name) {
        Label label = labelRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Label with name " + name + " not found"));
        return labelMapper.toDto(label);
    }

    @Transactional
    public LabelDTO create(LabelCreateDTO createDTO) {
        if (labelRepository.existsByName(createDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Label with name '" + createDTO.getName() + "' already exists");
        }
        Label label = labelMapper.toEntity(createDTO);
        labelRepository.save(label);
        return labelMapper.toDto(label);
    }

    @Transactional
    public LabelDTO update(Long id, LabelUpdateDTO updateDTO) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        if (updateDTO.getName() != null && updateDTO.getName().isPresent()) {
            String newName = updateDTO.getName().get();
            if (!newName.equals(label.getName()) && labelRepository.existsByName(newName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Label with name '" + newName + "' already exists");
            }
        }

        labelMapper.updateEntity(updateDTO, label);
        labelRepository.save(label);
        return labelMapper.toDto(label);
    }

    @Transactional
    public void delete(Long id) {
        if (labelRepository.existsByTasks(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete label with existing tasks");
        }
        labelRepository.deleteById(id);
    }
}
