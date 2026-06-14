package hexlet.code.mapper;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.model.Label;
import org.springframework.stereotype.Component;

@Component
public class LabelMapper {

    public Label toEntity(LabelCreateDTO dto) {
        if (dto == null) return null;
        Label label = new Label();
        label.setName(dto.getName());
        return label;
    }

    public LabelDTO toDto(Label label) {
        if (label == null) return null;
        LabelDTO dto = new LabelDTO();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setCreatedAt(label.getCreatedAt());
        return dto;
    }

    public void updateEntity(LabelUpdateDTO dto, Label label) {
        if (dto == null || label == null) return;
        if (dto.getName() != null && dto.getName().isPresent()) {
            label.setName(dto.getName().get());
        }
    }
}
