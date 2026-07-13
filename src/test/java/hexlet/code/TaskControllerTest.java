package hexlet.code;

import tools.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.config.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "jwt.secret=secret_key_for_jwt_token_generation_1234567890_hexlet_project",
        "jwt.expiration=86400000"
})
@AutoConfigureMockMvc
class TaskControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    private User testUser;
    private TaskStatus testStatus;
    private Label testLabel;
    private Task testTask;
    private String token;

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
        testUser.setEmail("test@example.com");
        testUser.setPasswordDigest(passwordEncoder.encode("password123"));
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);

        token = jwtService.generateToken(testUser.getEmail());

        testStatus = new TaskStatus();
        testStatus.setName("Draft");
        testStatus.setSlug("draft");
        taskStatusRepository.save(testStatus);

        testLabel = new Label();
        testLabel.setName("Bug");
        labelRepository.save(testLabel);

        testTask = new Task();
        testTask.setTitle("Test Task");
        testTask.setContent("Test Content");
        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(Set.of(testLabel));
        taskRepository.save(testTask);
    }

    @Test
    void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].status").value("draft"))
                .andExpect(jsonPath("$[0].assignee_id").value(testUser.getId().intValue()))
                .andExpect(jsonPath("$[0].taskLabelIds[0]").value(testLabel.getId().intValue()));
    }

    @Test
    void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", testTask.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.status").value("draft"))
                .andExpect(jsonPath("$.assignee_id").value(testUser.getId().intValue()));
    }

    @Test
    void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTask() throws Exception {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("New Task");
        dto.setContent("New Content");
        dto.setStatus("draft");
        dto.setAssigneeId(testUser.getId());
        dto.setTaskLabelIds(List.of(testLabel.getId()));

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.content").value("New Content"))
                .andExpect(jsonPath("$.status").value("draft"))
                .andExpect(jsonPath("$.assignee_id").value(testUser.getId().intValue()));

        assertThat(taskRepository.count()).isEqualTo(2);
    }

    @Test
    void testCreateTaskWithInvalidStatus() throws Exception {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("New Task");
        dto.setContent("New Content");
        dto.setStatus("invalid_status");

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskUpdateDTO dto = new TaskUpdateDTO();
        dto.setTitle(JsonNullable.of("Updated Task"));
        dto.setContent(JsonNullable.of("Updated Content"));

        mockMvc.perform(put("/api/tasks/{id}", testTask.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.content").value("Updated Content"));
    }

    @Test
    void testUpdateTaskAssignee() throws Exception {
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setPasswordDigest(passwordEncoder.encode("password123"));
        anotherUser.setFirstName("Another");
        anotherUser.setLastName("User");
        userRepository.save(anotherUser);

        TaskUpdateDTO dto = new TaskUpdateDTO();
        dto.setAssigneeId(JsonNullable.of(anotherUser.getId()));

        mockMvc.perform(put("/api/tasks/{id}", testTask.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignee_id").value(anotherUser.getId().intValue()));

        Task updated = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(updated.getAssignee().getId()).isEqualTo(anotherUser.getId());
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", testTask.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }

    @Test
    void testFilterTasksByTitle() throws Exception {
        mockMvc.perform(get("/api/tasks?titleCont=Test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testFilterTasksByAssignee() throws Exception {
        mockMvc.perform(get("/api/tasks?assigneeId=" + testUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].assignee_id").value(testUser.getId().intValue()));
    }

    @Test
    void testFilterTasksByStatus() throws Exception {
        mockMvc.perform(get("/api/tasks?status=draft")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("draft"));
    }

    @Test
    void testFilterTasksByLabel() throws Exception {
        mockMvc.perform(get("/api/tasks?labelId=" + testLabel.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].taskLabelIds[0]").value(testLabel.getId().intValue()));
    }
}
