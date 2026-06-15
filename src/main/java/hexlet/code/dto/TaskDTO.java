package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private LocalDate createdAt;
    private Long assigneeId;
    private String title;
    private String content;
    private String status;

    @JsonProperty("taskLabelIds")
    private List<Long> labelIds;
}


