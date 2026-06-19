package hexlet.code;

import tools.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "jwt.secret=secret_key_for_jwt_token_generation_1234567890_hexlet_project",
        "jwt.expiration=86400000"
})
@AutoConfigureMockMvc
class TaskStatusControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testCreate() throws Exception {
        TaskStatusCreateDTO dto = new TaskStatusCreateDTO();
        dto.setName("New Status");
        dto.setSlug("new_status");

        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Status"))
                .andExpect(jsonPath("$.slug").value("new_status"));
    }

    @Test
    @WithMockUser
    void testCreateWithDuplicateSlug() throws Exception {
        TaskStatusCreateDTO dto = new TaskStatusCreateDTO();
        dto.setName("First Status");
        dto.setSlug("first_status");

        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        TaskStatusCreateDTO duplicateDto = new TaskStatusCreateDTO();
        duplicateDto.setName("Second Status");
        duplicateDto.setSlug("first_status");

        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("already exists")));
    }
}


