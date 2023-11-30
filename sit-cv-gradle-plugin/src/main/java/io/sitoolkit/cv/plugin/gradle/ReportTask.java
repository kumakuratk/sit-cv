package io.sitoolkit.cv.plugin.gradle;

import io.sitoolkit.cv.core.app.config.ServiceFactory;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

public class ReportTask extends DefaultTask {

  private boolean analyzeSql;
  private String testTarget;

  @Option(option = RunTask.ANALYZE_SQL_OPTION, description = RunTask.ANALYZE_SQL_DESCRIPTION)
  public void setAnalyzeSql(boolean analyzeSql) {
    this.analyzeSql = analyzeSql;
  }

  @Option(option = RunTask.TEST_TARGET_OPTION, description = RunTask.ANALYZE_SQL_DESCRIPTION)
  public void setTestTarget(String testTarget) {
    this.testTarget = testTarget;
  }

  @TaskAction
  public void export() {
    ServiceFactory factory =
        ServiceFactory.createAndInitialize(getProject().getProjectDir().toPath(), false);

    if (analyzeSql) {
      factory.getCrudService().analyzeSql(testTarget);
    }

    factory.getReportService().export();
  }
}
