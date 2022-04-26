package ch.cern.todo;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

  private static final String TASKS_URL_API = "/tasks";
  private static final String PREFIX_FILE_NAME = "src/test/resources/ch/cern/todo/task/";

  @Autowired private MockMvc mockMvc;

  @Test
  void test_get_tasks_when_tasks_exist() throws Exception {

    MvcResult mvcResult =
        mockMvc.perform(get(TASKS_URL_API)).andDo(print()).andExpect(status().isOk()).andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();

    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_get_all_tasks.json");

    JSONAssert.assertEquals(actualResponse, expectedResponse, JSONCompareMode.LENIENT);
  }

  @Test
  void test_get_tasks_of_category_other() throws Exception {

    MvcResult mvcResult =
        mockMvc
            .perform(get(TASKS_URL_API + "?category=other"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();

    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_get_other_tasks.json");

    JSONAssert.assertEquals(actualResponse, expectedResponse, JSONCompareMode.LENIENT);
  }

  @Test
  void test_add_new_task_when_input_is_ok() throws Exception {
    String request = TestUtils.readFile(PREFIX_FILE_NAME + "/request/new_task_ok_request.json");

    MvcResult mvcResult =
        mockMvc
            .perform(post(TASKS_URL_API).contentType(MediaType.APPLICATION_JSON).content(request))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "/response/new_task_ok_response.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }
}
