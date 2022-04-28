package ch.cern.todo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static ch.cern.todo.TestUtils.TASKS_URL_API;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerQueryTasksIntegrationTest {

  private static final String PREFIX_FILE_NAME = "src/test/resources/ch/cern/todo/task/";

  @Autowired private MockMvc mockMvc;

  @ParameterizedTest
  @MethodSource("provideArguments")
  void test_get_tasks_by_criteria(String fileNameRequest, String fileNameExpectedResponse)
      throws Exception {

    String request = TestUtils.readFile(fileNameRequest);

    MvcResult mvcResult =
        mockMvc
            .perform(get(TASKS_URL_API).contentType(MediaType.APPLICATION_JSON).content(request))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse = TestUtils.readFile(fileNameExpectedResponse);

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.STRICT_ORDER);
  }

  private static Stream<Arguments> provideArguments() {
    return Stream.of(
        Arguments.of(
            PREFIX_FILE_NAME + "/request/get_all_tasks_request.json",
            PREFIX_FILE_NAME + "/response/get_all_tasks_response.json"),
        Arguments.of(
            PREFIX_FILE_NAME + "/request/get_other_tasks_request.json",
            PREFIX_FILE_NAME + "/response/get_other_tasks_response.json"),
        Arguments.of(
            PREFIX_FILE_NAME + "/request/get_other_and_wellness_tasks_request.json",
            PREFIX_FILE_NAME + "/response/get_other_and_wellness_tasks_response.json"),
        Arguments.of(
            PREFIX_FILE_NAME + "/request/get_all_tasks_with_deadline_request.json",
            PREFIX_FILE_NAME + "/response/get_all_tasks_with_deadline_response.json"),
        Arguments.of(
            PREFIX_FILE_NAME + "/request/get_other_tasks_with_deadline_request.json",
            PREFIX_FILE_NAME + "/response/get_other_tasks_with_deadline_response.json"));
  }
}
