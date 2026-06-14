package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TaskStatusRepository taskStatusRepository;

    @Autowired
    protected LabelRepository labelRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusToModel")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "userToModel")
    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "labelsToModel")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "labelsToIds")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusToModelAsync")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "userToModelAsync")
    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "labelsToModelAsync")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("statusToModel")
    protected TaskStatus statusToModel(String slug) {
        if (slug == null) return null;
        return taskStatusRepository.findBySlug(slug).orElse(null);
    }

    @Named("userToModel")
    protected User userToModel(Long id) {
        if (id == null) return null;
        return userRepository.findById(id).orElse(null);
    }

    @Named("labelsToModel")
    protected List<Label> labelsToModel(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> labelRepository.findById(id).orElse(null))
                .toList();
    }

    @Named("labelsToIds")
    protected List<Long> labelsToIds(List<Label> labels) {
        if (labels == null) return null;
        return labels.stream()
                .map(Label::getId)
                .toList();
    }

    @Named("statusToModelAsync")
    protected TaskStatus statusToModelAsync(JsonNullable<String> slug) {
        if (slug == null || !slug.isPresent()) return null;
        return statusToModel(slug.orElse(null));
    }

    @Named("userToModelAsync")
    protected User userToModelAsync(JsonNullable<Long> id) {
        if (id == null || !id.isPresent()) return null;
        return userToModel(id.orElse(null));
    }

    @Named("labelsToModelAsync")
    protected List<Label> labelsToModelAsync(JsonNullable<List<Long>> ids) {
        if (ids == null || !ids.isPresent()) return null;
        return labelsToModel(ids.orElse(null));
    }
}

