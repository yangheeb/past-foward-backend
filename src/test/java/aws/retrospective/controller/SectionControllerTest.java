package aws.retrospective.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aws.retrospective.dto.CreateSectionRequest;
import aws.retrospective.service.CommentService;
import aws.retrospective.service.SectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser("user1")
@WebMvcTest(controllers = SectionController.class)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SectionService sectionService;
    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("신규 회고카드를 등록한다.")
    void createSection() throws Exception {
        //given
        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(1L)
            .templateSectionId(2L)
            .sectionContent("내용")
            .build();

        //when //then
        mockMvc.perform(
                post("/sections")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.message").doesNotExist());
    }

}