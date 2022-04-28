package ch.cern.todo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {

  static final String TASKS_URL_API = "/tasks";
  static final String CATEGORIES_URL_API = "/categories";

  static String readFile(String fileName) throws IOException {
    Path path = Paths.get(fileName);

    Stream<String> lines = Files.lines(path);
    String content = lines.collect(Collectors.joining("\n"));
    lines.close();

    return content;
  }

  static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
