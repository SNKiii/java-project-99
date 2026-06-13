package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDTO getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO getByName(String name) {
        Label label = labelRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Label with name " + name + " not found"));
        return labelMapper.map(label);
    }

    @Transactional
    public LabelDTO create(LabelCreateDTO createDTO) {
        Label label = labelMapper.map(createDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @Transactional
    public LabelDTO update(Long id, LabelUpdateDTO updateDTO) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(updateDTO, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @Transactional
    public void delete(Long id) {
        if (labelRepository.existsByTasks(id)) {
            throw new RuntimeException("Cannot delete label with existing tasks");
        }
        labelRepository.deleteById(id);
    }
}
