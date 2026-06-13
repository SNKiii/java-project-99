package hexlet.code;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "hexlet@example.com", roles = {"USER"})
    void testFilterByTitle() throws Exception {
        mockMvc.perform(get("/api/tasks?titleCont=test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFilterByAssigneeId() throws Exception {
        mockMvc.perform(get("/api/tasks?assigneeId=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFilterByStatus() throws Exception {
        mockMvc.perform(get("/api/tasks?status=draft"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFilterByLabelId() throws Exception {
        mockMvc.perform(get("/api/tasks?labelId=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFilterByAllParams() throws Exception {
        mockMvc.perform(get("/api/tasks?titleCont=test&assigneeId=1&status=draft&labelId=1"))
                .andExpect(status().isOk());
    }
}
