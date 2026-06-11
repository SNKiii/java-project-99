package hexlet.code.mapper;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class LabelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    public abstract Label map(LabelCreateDTO dto);

    public abstract LabelDTO map(Label model);

    @Mapping(target = "name", source = "name", qualifiedByName = "fromJsonNullable")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    public abstract void update(LabelUpdateDTO dto, @MappingTarget Label model);

    @Named("fromJsonNullable")
    public String fromJsonNullable(JsonNullable<String> jsonNullable) {
        return jsonNullable != null ? jsonNullable.orElse(null) : null;
    }
}
