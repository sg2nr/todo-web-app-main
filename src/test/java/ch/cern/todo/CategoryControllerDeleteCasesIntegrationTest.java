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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryControllerDeleteCasesIntegrationTest {

  private static final String CATEGORY_WITHOUT_TASK = "foo";

  private static final String CATEGORY_WITH_TASK = "wellness";

  private static final String CATEGORY_INVALID = "random";

  @Autowired private MockMvc mockMvc;

  @Test
  void test_delete_category_when_it_does_not_have_tasks_it_should_return_status_204()
      throws Exception {
    mockMvc
        .perform(delete(TestUtils.CATEGORIES_URL_API + "/" + CATEGORY_WITHOUT_TASK))
        .andDo(print())
        .andExpect(status().is(204));
  }

  @Test
  void test_delete_category_when_it_has_tasks_it_should_return_an_error() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(delete(TestUtils.CATEGORIES_URL_API + "/" + CATEGORY_WITH_TASK))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(
            "src/test/resources/ch/cern/todo/category/expected_response_delete_category_with_tasks.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  void test_delete_category_when_it_does_not_exist_it_should_return_an_error() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(delete(TestUtils.CATEGORIES_URL_API + "/" + CATEGORY_INVALID))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(
            "src/test/resources/ch/cern/todo/category/expected_response_delete_category_not_existing.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }
}
