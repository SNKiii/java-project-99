package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "labelIdsToLabels")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "slugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "idToUser")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "labelsToLabelIds")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "jsonNullableLabelIdsToLabels")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "jsonNullableSlugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "jsonNullableIdToUser")
    @Mapping(target = "title", source = "title", qualifiedByName = "jsonNullableStringToString")
    @Mapping(target = "content", source = "content", qualifiedByName = "jsonNullableStringToString")
    @Mapping(target = "index", source = "index", qualifiedByName = "jsonNullableIntegerToInteger")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("slugToTaskStatus")
    public TaskStatus slugToTaskStatus(String slug) {
        if (slug == null) {
            return null;
        }
        return taskStatusRepository.findBySlug(slug).orElse(null);
    }

    @Named("idToUser")
    public User idToUser(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }

    @Named("labelIdsToLabels")
    public List<Label> labelIdsToLabels(List<Long> labelIds) {
        if (labelIds == null) {
            return null;
        }
        return labelIds.stream()
                .map(id -> labelRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Named("labelsToLabelIds")
    public List<Long> labelsToLabelIds(List<Label> labels) {
        if (labels == null) {
            return null;
        }
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toList());
    }

    @Named("jsonNullableSlugToTaskStatus")
    public TaskStatus jsonNullableSlugToTaskStatus(JsonNullable<String> slug) {
        if (slug == null || !slug.isPresent()) {
            return null;
        }
        return taskStatusRepository.findBySlug(slug.get()).orElse(null);
    }

    @Named("jsonNullableIdToUser")
    public User jsonNullableIdToUser(JsonNullable<Long> id) {
        if (id == null || !id.isPresent()) {
            return null;
        }
        return userRepository.findById(id.get()).orElse(null);
    }

    @Named("jsonNullableLabelIdsToLabels")
    public List<Label> jsonNullableLabelIdsToLabels(JsonNullable<List<Long>> labelIds) {
        if (labelIds == null || !labelIds.isPresent()) {
            return null;
        }
        return labelIdsToLabels(labelIds.get());
    }

    @Named("jsonNullableStringToString")
    public String jsonNullableStringToString(JsonNullable<String> jsonNullable) {
        if (jsonNullable == null) {
            return null;
        }
        return jsonNullable.orElse(null);
    }

    @Named("jsonNullableIntegerToInteger")
    public Integer jsonNullableIntegerToInteger(JsonNullable<Integer> jsonNullable) {
        if (jsonNullable == null) {
            return null;
        }
        return jsonNullable.orElse(null);
    }
}
