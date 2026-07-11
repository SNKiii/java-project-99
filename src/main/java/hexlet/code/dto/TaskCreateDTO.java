package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank
    private String title;
    private String content;
    @NotBlank
    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private List<Long> taskLabelIds;
}

