package ch.cern.todo;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerUpdateTaskIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void test_update_task() throws Exception {
    String taskId = "4";

    String patchRequest =
        TestUtils.readFile(
            "src/test/resources/ch/cern/todo/task/request/patch_task_ok_request.json");

    MvcResult mvcResult =
        mockMvc
            .perform(
                patch(TestUtils.TASKS_URL_API + "/" + taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(patchRequest))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(
            "src/test/resources/ch/cern/todo/task/response/patch_task_ok_response.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
  }
}
