package io.sitoolkit.cv.plugin.gradle;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.commons.io.file.PathUtils;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ReportTaskTest {

  @Test
  public void testExport(@TempDir Path tempDir) {
    Path testDir = Paths.get("../test-projects/gradle-multi");
    Path projectDir = null;
    try {
      projectDir = Files.createDirectory(Paths.get(tempDir.toString(), "report"));
      PathUtils.copyDirectory(testDir, projectDir);
    } catch (IOException e) {
      e.printStackTrace();
    }

    GradleRunner.create()
        .withProjectDir(projectDir.toFile())
        .withArguments(Arrays.asList("cvReport"))
        .build();

    assertTrue(Files.exists(Paths.get(projectDir.toString(), "docs")));
  }
}
