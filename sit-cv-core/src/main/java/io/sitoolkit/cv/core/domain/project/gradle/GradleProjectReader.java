package io.sitoolkit.cv.core.domain.project.gradle;

import io.sitoolkit.cv.core.domain.project.Project;
import io.sitoolkit.cv.core.domain.project.ProjectReader;
import io.sitoolkit.cv.core.domain.project.analyze.SqlLogProcessor;
import io.sitoolkit.cv.core.infra.config.CvConfig;
import io.sitoolkit.cv.core.infra.project.SitCvToolsManager;
import io.sitoolkit.cv.core.infra.util.SitResourceUtils;
import io.sitoolkit.util.buildtoolhelper.gradle.GradleProject;
import io.sitoolkit.util.buildtoolhelper.process.ProcessCommand;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class GradleProjectReader implements ProjectReader {

  private static final String PROJECT_INFO_SCRIPT_NAME = "project-info.gradle";

  @NonNull private SqlLogProcessor sqlLogProcessor;

  @Override
  public Optional<Project> read(Path projectDir) {

    GradleProject gradleProject = GradleProject.load(projectDir);

    if (!gradleProject.available()) {
      return Optional.empty();
    }

    GradleProjectInfoListener listener = new GradleProjectInfoListener(projectDir);

    log.info("project: {} is a gradle project - finding depending jars... ", projectDir);

    Path initScript = projectDir.resolve(PROJECT_INFO_SCRIPT_NAME);
    SitResourceUtils.res2file(this, PROJECT_INFO_SCRIPT_NAME, initScript);

    try {

      gradleProject
          .gradlew("--no-daemon", "--init-script", initScript.toString(), "projectInfo")
          .stdout(listener)
          .execute();

    } finally {
      try {
        Files.deleteIfExists(initScript);
      } catch (IOException e) {
        log.warn(e.getMessage());
      }
    }

    return Optional.of(listener.getProject());
  }

  @Override
  public boolean generateSqlLog(Project project, CvConfig sitCvConfig, String testTarget) {
    log.info("TODO test: project={}", project);
    GradleProject gradleProject = GradleProject.load(project.getDir());

    if (!gradleProject.available()) {
      return false;
    }

    Path agentJar = SitCvToolsManager.install(project.getWorkDir(), project.getJavaVersion());

    sqlLogProcessor.process(
        "gradle",
        sitCvConfig,
        agentJar,
        project,
        (String agentParam) -> {
          String[] gradleArgs = new String[] {"--no-daemon", "--rerun-tasks", "test"};
          if (StringUtils.isNotBlank(testTarget)) {
            gradleArgs = ArrayUtils.addAll(gradleArgs, "--tests", testTarget);
          }

          ProcessCommand command = gradleProject.gradlew(gradleArgs);
          command.getEnv().put("JAVA_TOOL_OPTIONS", agentParam);
          return command;
        });

    return true;
  }
}
