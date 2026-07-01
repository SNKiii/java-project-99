package hexlet.code.service;

import hexlet.code.dto.LabelDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Transactional
    public LabelDTO update(LabelDTO modelUpdate, Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        Optional.ofNullable(modelUpdate.getName())
                .ifPresent(label::setName);

        labelRepository.save(label);
        return labelMapper.map(label);
    }
}