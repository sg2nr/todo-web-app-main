package ch.cern.todo;

import ch.cern.todo.model.Category;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryControllerIntegrationTest {

  private static final String PREFIX_FILE_NAME = "src/test/resources/ch/cern/todo/category/";
  private static final String URL_CATEGORIES_API = "/categories";
  @Autowired private MockMvc mockMvc;

  @Test
  void test_get_categories_when_categories_exist() throws Exception {

    MvcResult mvcResult =
        mockMvc
            .perform(get(URL_CATEGORIES_API))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();

    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_get_categories.json");

    JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
  }

  @Test
  void test_add_category_already_existing() throws Exception {

    // Given a category named "job"
    Category jobCategory = new Category();
    jobCategory.setName("job");

    // add it the first time -> OK
    mockMvc
        .perform(
            post(URL_CATEGORIES_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(jobCategory)))
        .andDo(print())
        .andExpect(status().is(201));

    // add it the second time -> Error
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(URL_CATEGORIES_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.asJsonString(jobCategory)))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_existing_category_name.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  void test_add_empty_category_should_return_an_error() throws Exception {

    Category emptyCategory = new Category();
    emptyCategory.setName("");
    emptyCategory.setDescription("");

    MvcResult mvcResult =
        mockMvc
            .perform(
                post(URL_CATEGORIES_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.asJsonString(emptyCategory)))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        TestUtils.readFile(PREFIX_FILE_NAME + "expected_response_missing_category_name.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }
}
