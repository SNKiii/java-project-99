package hexlet.code;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerFilterTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    private User testUser;
    private TaskStatus testStatus;
    private Label testLabel;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("hexlet@example.com");
        testUser.setPasswordDigest("password");
        userRepository.save(testUser);

        testStatus = new TaskStatus();
        testStatus.setName("Draft");
        testStatus.setSlug("draft");
        taskStatusRepository.save(testStatus);

        testLabel = new Label();
        testLabel.setName("Bug");
        labelRepository.save(testLabel);

        Task task = new Task();
        task.setTitle("test task");
        task.setContent("content");
        task.setTaskStatus(testStatus);
        task.setAssignee(testUser);
        task.setLabels(List.of(testLabel));
        taskRepository.save(task);
    }

    @Test
    @WithMockUser(username = "hexlet@example.com")
    void testFilterByTitle() throws Exception {
        mockMvc.perform(get("/api/tasks?titleCont=test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hexlet@example.com")
    void testFilterByAssigneeId() throws Exception {
        mockMvc.perform(get("/api/tasks?assigneeId=" + testUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hexlet@example.com")
    void testFilterByStatus() throws Exception {
        mockMvc.perform(get("/api/tasks?status=" + testStatus.getSlug()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hexlet@example.com")
    void testFilterByLabelId() throws Exception {
        mockMvc.perform(get("/api/tasks?labelId=" + testLabel.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hexlet@example.com")
    void testFilterByAllParams() throws Exception {
        mockMvc.perform(get("/api/tasks?titleCont=test&assigneeId=" + testUser.getId()
                        + "&status=" + testStatus.getSlug() + "&labelId=" + testLabel.getId()))
                .andExpect(status().isOk());
    }
}




