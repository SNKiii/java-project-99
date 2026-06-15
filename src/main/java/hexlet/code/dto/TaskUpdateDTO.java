package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<String> status;
    private JsonNullable<Long> assigneeId;
    private JsonNullable<List<Long>> taskLabelIds;
}

