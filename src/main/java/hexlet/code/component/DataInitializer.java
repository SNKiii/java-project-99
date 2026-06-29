package hexlet.code.component;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.service.LabelServiceInterface;
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
    private final LabelServiceInterface labelService;

    @Override
    public void run(ApplicationArguments args) {
        createAdminUser();
        createDefaultStatuses();
        createDefaultLabels();
    }

    private void createAdminUser() {
        try {
            userService.getByEmail("hexlet@example.com");
            System.out.println("Admin user already exists: hexlet@example.com");
        } catch (Exception e) {
            UserCreateDTO admin = new UserCreateDTO();
            admin.setEmail("hexlet@example.com");
            admin.setPassword("qwerty");
            admin.setFirstName("Admin");
            admin.setLastName("Hexlet");
            userService.create(admin);
            System.out.println("Admin user created: hexlet@example.com / qwerty");
        }
    }

    private void createDefaultStatuses() {
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
                System.out.println("Task status already exists: " + statusDTO.getName());
            } catch (Exception e) {
                taskStatusService.create(statusDTO);
                System.out.println("Task status created: " + statusDTO.getName());
            }
        }
    }

    private void createDefaultLabels() {
        List<LabelCreateDTO> defaultLabels = List.of(
                createLabel("feature"),
                createLabel("bug")
        );

        for (LabelCreateDTO labelDTO : defaultLabels) {
            try {
                labelService.getByName(labelDTO.getName());
                System.out.println("Label already exists: " + labelDTO.getName());
            } catch (Exception e) {
                labelService.create(labelDTO);
                System.out.println("Label created: " + labelDTO.getName());
            }
        }
    }

    private TaskStatusCreateDTO createStatus(String name, String slug) {
        TaskStatusCreateDTO dto = new TaskStatusCreateDTO();
        dto.setName(name);
        dto.setSlug(slug);
        return dto;
    }

    private LabelCreateDTO createLabel(String name) {
        LabelCreateDTO dto = new LabelCreateDTO();
        dto.setName(name);
        return dto;
    }
}