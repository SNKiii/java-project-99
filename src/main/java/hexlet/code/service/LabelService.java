package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService implements LabelServiceInterface {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LabelDTO> getAll() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::map)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LabelDTO getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    @Override
    @Transactional(readOnly = true)
    public LabelDTO getByName(String name) {
        Label label = labelRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Label with name " + name + " not found"));
        return labelMapper.map(label);
    }

    @Override
    @Transactional
    public LabelDTO create(LabelCreateDTO createDTO) {
        Label label = labelMapper.map(createDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @Override
    @Transactional
    public LabelDTO update(Long id, LabelUpdateDTO updateDTO) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        labelMapper.update(updateDTO, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}