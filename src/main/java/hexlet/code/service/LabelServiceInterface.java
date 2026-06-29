package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;

import java.util.List;

public interface LabelServiceInterface {
    List<LabelDTO> getAll();
    LabelDTO getById(Long id);
    LabelDTO getByName(String name);
    LabelDTO create(LabelCreateDTO createDTO);
    LabelDTO update(Long id, LabelUpdateDTO updateDTO);
    void delete(Long id);
}