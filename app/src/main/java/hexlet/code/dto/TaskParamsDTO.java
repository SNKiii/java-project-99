package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TaskParamsDTO {
    private String titleCont;
    private Long assigneeId;
    private String status;
    private Long labelId;
}
