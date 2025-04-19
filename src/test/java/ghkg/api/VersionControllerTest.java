package ghkg.api;

import ghkg.config.ApiPaths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(VersionController.class)
class VersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitProperties gitProperties;

    @Test
    void shouldReturnVersionInfo() throws Exception {
        when(gitProperties.get("build.version")).thenReturn("1.0.0");
        when(gitProperties.get("commit.id.abbrev")).thenReturn("abc1234");
        when(gitProperties.get("branch")).thenReturn("main");
        when(gitProperties.get("commit.time")).thenReturn("2025-04-18T22:55:17+0200");

        mockMvc.perform(get(ApiPaths.VERSION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.commit").value("abc1234"))
                .andExpect(jsonPath("$.branch").value("main"))
                .andExpect(jsonPath("$.time").value("2025-04-18T22:55:17+0200"));
    }
}
