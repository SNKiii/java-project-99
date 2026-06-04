package hexlet.code.component;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            userService.getByEmail("hexlet@example.com");
        } catch (Exception e) {
            UserCreateDTO admin = new UserCreateDTO();
            admin.setEmail("hexlet@example.com");
            admin.setPassword("qwerty");
            admin.setFirstName("Admin");
            admin.setLastName("Hexlet");
            userService.create(admin);
        }
    }
}