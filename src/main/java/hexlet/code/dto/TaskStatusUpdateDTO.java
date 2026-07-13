package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatusUpdateDTO {

    @Size(min = 1)
    private JsonNullable<String> name;

    @Size(min = 1)
    private JsonNullable<String> slug;
}
