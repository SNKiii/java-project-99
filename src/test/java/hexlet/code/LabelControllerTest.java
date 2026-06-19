package hexlet.code;

import tools.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.config.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "jwt.secret=secret_key_for_jwt_token_generation_1234567890_hexlet_project",
        "jwt.expiration=86400000"
})
@AutoConfigureMockMvc
class LabelControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    private Label testLabel;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        labelRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordDigest(passwordEncoder.encode("password123"));
        user.setFirstName("Test");
        user.setLastName("User");
        userRepository.save(user);

        testLabel = new Label();
        testLabel.setName("Test Label");
        labelRepository.save(testLabel);
    }

    @Test
    @WithMockUser
    void testGetAllLabels() throws Exception {
        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Label"));
    }

    @Test
    @WithMockUser
    void testGetLabelById() throws Exception {
        mockMvc.perform(get("/api/labels/{id}", testLabel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Label"));
    }

    @Test
    @WithMockUser
    void testGetLabelByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/labels/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCreateLabel() throws Exception {
        LabelCreateDTO dto = new LabelCreateDTO();
        dto.setName("New Label");

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Label"));

        assertThat(labelRepository.count()).isEqualTo(2);
    }

    @Test
    @WithMockUser
    void testCreateLabelWithDuplicateName() throws Exception {
        LabelCreateDTO dto = new LabelCreateDTO();
        dto.setName("Test Label");

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void testUpdateLabel() throws Exception {
        LabelUpdateDTO dto = new LabelUpdateDTO();
        dto.setName(JsonNullable.of("Updated Label"));

        mockMvc.perform(put("/api/labels/{id}", testLabel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Label"));
    }

    @Test
    @WithMockUser
    void testDeleteLabel() throws Exception {
        mockMvc.perform(delete("/api/labels/{id}", testLabel.getId()))
                .andExpect(status().isNoContent());

        assertThat(labelRepository.findById(testLabel.getId())).isEmpty();
    }
}