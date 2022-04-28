package ch.cern.todo;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static ch.cern.todo.TestUtils.TASKS_URL_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerDeleteCasesIntegrationTest {

  private static final String PREFIX_FILE_NAME = "src/test/resources/ch/cern/todo/task/";
  private static final String TASK_ID_1 = "1";
  private static final String TASK_ID_NOT_EXISTING = "100";

  @Autowired private MockMvc mockMvc;

  @Test
  void test_delete_task_when_it_exists_should_return_status_204() throws Exception {
    mockMvc.perform(get(TASKS_URL_API + "/" + TASK_ID_1)).andDo(print()).andExpect(status().isOk());

    mockMvc
        .perform(delete(TASKS_URL_API + "/" + TASK_ID_1))
        .andDo(print())
        .andExpect(status().is(204));
  }

  @Test
  void test_delete_task_when_it_does_not_exist_it_should_return_an_error() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(delete(TASKS_URL_API + "/" + TASK_ID_NOT_EXISTING))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(
            PREFIX_FILE_NAME + "/expected_response_get_task_by_id_not_existing.json");

    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }
}
