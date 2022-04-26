package ch.cern.todo;

import ch.cern.todo.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  @Autowired private MockMvc mockMvc;

  @Test
  void test_get_categories_when_categories_exist() throws Exception {

    MvcResult mvcResult =
        mockMvc.perform(get("/categories")).andDo(print()).andExpect(status().isOk()).andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();

    String expectedResponse =
        readFile("src/test/resources/ch/cern/todo/expected_response_get_categories.json");

    JSONAssert.assertEquals(actualResponse, expectedResponse, JSONCompareMode.LENIENT);
  }

  @Test
  void test_add_category_already_existing() throws Exception {

    // Given a category named "job"
    Category jobCategory = new Category();
    jobCategory.setName("job");

    // add it the first time -> OK
    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jobCategory)))
        .andDo(print())
        .andExpect(status().isOk());

    // add it the second time -> Error
    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jobCategory)))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  void test_add_empty_category_should_return_an_error() throws Exception {

    Category emptyCategory = new Category();
    emptyCategory.setName("");
    emptyCategory.setDescription("");

    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(emptyCategory)))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andReturn();

    String actualResponse = mvcResult.getResponse().getContentAsString();
    String expectedResponse =
        readFile("src/test/resources/ch/cern/todo/expected_response_missing_category_name.json");

    assertThat(actualResponse).isNotEmpty();
    JSONAssert.assertEquals(
        expectedResponse,
        actualResponse,
        new CustomComparator(
            JSONCompareMode.LENIENT, new Customization("timestamp", (o1, o2) -> true)));
  }

  private static String readFile(String fileName) throws IOException {
    Path path = Paths.get(fileName);

    Stream<String> lines = Files.lines(path);
    String expectedResponse = lines.collect(Collectors.joining("\n"));
    lines.close();

    return expectedResponse;
  }

  private static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
