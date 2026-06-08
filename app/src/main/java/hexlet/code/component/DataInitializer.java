package hexlet.code.component;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final TaskStatusService taskStatusService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            userService.getByEmail("hexlet@example.com");
        } catch (Exception e) {
            UserCreateDTO admin = new UserCreateDTO();
            admin.setEmail("hexlet@example.com");
            admin.setPassword("qwerty");
            admin.setFirstName("Admin");
            admin.setLastName("Hexlet");
            userService.create(admin);
            System.out.println("Admin user created: hexlet@example.com / qwerty");
        }

        List<TaskStatusCreateDTO> defaultStatuses = List.of(
                createStatus("Draft", "draft"),
                createStatus("ToReview", "to_review"),
                createStatus("ToBeFixed", "to_be_fixed"),
                createStatus("ToPublish", "to_publish"),
                createStatus("Published", "published")
        );

        for (TaskStatusCreateDTO statusDTO : defaultStatuses) {
            try {
                taskStatusService.getBySlug(statusDTO.getSlug());
            } catch (Exception e) {
                taskStatusService.create(statusDTO);
                System.out.println("Task status created: " + statusDTO.getName());
            }
        }
    }

    private TaskStatusCreateDTO createStatus(String name, String slug) {
        TaskStatusCreateDTO dto = new TaskStatusCreateDTO();
        dto.setName(name);
        dto.setSlug(slug);
        return dto;
    }
}