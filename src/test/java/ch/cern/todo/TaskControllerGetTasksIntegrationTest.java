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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerGetTasksIntegrationTest {

  private static final String PREFIX_FILE_NAME = "src/test/resources/ch/cern/todo/task/";

  @Autowired private MockMvc mockMvc;

  @Test
  void test_get_task_by_id() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(get(TASKS_URL_API + "/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_get_task_1.json");

    JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.STRICT_ORDER);
  }

  @Test
  void test_get_task_by_id_not_existing_it_should_return_an_error() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(get(TASKS_URL_API + "/100"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_get_task_by_id_not_existing.json");

    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  void test_get_task_by_invalid_id_it_should_return_an_error() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(get(TASKS_URL_API + "/f00"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_get_task_by_id_not_numeric.json");

    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }
}
