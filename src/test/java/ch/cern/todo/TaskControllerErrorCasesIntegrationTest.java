package ch.cern.todo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerErrorCasesIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @ParameterizedTest
  @MethodSource("provideErrorCases")
  void test_invalid_input_data(String fileNameRequest, String fileNameExpectedResponse)
      throws Exception {

    String request = TestUtils.readFile(fileNameRequest);

    MvcResult mvcResult =
        mockMvc
            .perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(request))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse = TestUtils.readFile(fileNameExpectedResponse);

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }

  private static Stream<Arguments> provideErrorCases() {
    return Stream.of(
        Arguments.of(
            "src/test/resources/ch/cern/todo/task/request/new_task_without_name_request.json",
            "src/test/resources/ch/cern/todo/task/response/new_task_without_name_response.json"),
        Arguments.of(
            "src/test/resources/ch/cern/todo/task/request/new_task_without_deadline_request.json",
            "src/test/resources/ch/cern/todo/task/response/new_task_without_deadline_response.json"),
        Arguments.of(
            "src/test/resources/ch/cern/todo/task/request/new_task_without_category_request.json",
            "src/test/resources/ch/cern/todo/task/response/new_task_without_category_response.json"),
        Arguments.of(
            "src/test/resources/ch/cern/todo/task/request/new_task_with_non_existing_category_request.json",
            "src/test/resources/ch/cern/todo/task/response/new_task_with_non_existing_category_response.json"));
  }
}
