package hexlet.code;

import tools.jackson.databind.ObjectMapper;
import hexlet.code.dto.AuthRequestDTO;
import hexlet.code.model.User;
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

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "jwt.secret=secret_key_for_jwt_token_generation_1234567890_hexlet_project",
        "jwt.expiration=86400000"
})
@AutoConfigureMockMvc
class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordDigest(passwordEncoder.encode("password123"));
        user.setFirstName("Test");
        user.setLastName("User");
        userRepository.save(user);
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("test@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyOrNullString())));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("test@example.com");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithEmptyUsername() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("");
        request.setPassword("password123");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithEmptyPassword() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("test@example.com");
        request.setPassword("");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}