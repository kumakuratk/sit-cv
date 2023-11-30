package io.sitoolkit.cv.app;

import io.sitoolkit.cv.app.infra.config.SitCvApplicationOption;
import io.sitoolkit.cv.app.infra.utils.DesktopManager;
import io.sitoolkit.cv.core.app.config.ServiceFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@AllArgsConstructor
public class SitCvApplication {

  private final ApplicationContext appCtx;

  public static void main(String[] args) {
    ApplicationArguments appArgs = new DefaultApplicationArguments(args);

    if (appArgs.containsOption(SitCvApplicationOption.ANALYZE_SQL.getKey())
        && !"restartedMain".equals(Thread.currentThread().getName())) {
      executeAnalyzeSqlMode(appArgs);
    }

    if (appArgs.containsOption(SitCvApplicationOption.REPORT.getKey())) {
      executeReportMode(appArgs);
    } else {
      executeServerMode(args, appArgs);
    }
  }

  private static Path getProjectDir(ApplicationArguments appArgs) {
    String projectPath = SitCvApplicationOption.PROJECT.getValue(appArgs, ".");
    return Path.of(projectPath).toAbsolutePath().normalize();
  }

  private static void executeReportMode(ApplicationArguments appArgs) {
    ServiceFactory.createAndInitialize(getProjectDir(appArgs), false).getReportService().export();
  }

  private static void executeAnalyzeSqlMode(ApplicationArguments appArgs) {
    List<String> testOptions = appArgs.getOptionValues(SitCvApplicationOption.TEST.getKey());
    String testTarget =
        Objects.nonNull(testOptions) ? testOptions.stream().findFirst().orElse("") : "";

    ServiceFactory.create(getProjectDir(appArgs), false).getCrudService().analyzeSql(testTarget);
  }

  private static void executeServerMode(String[] args, ApplicationArguments appArgs) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(SitCvApplication.class);
    ApplicationContext appCtx = builder.headless(false).run(args);

    if (needsOpenBrowser(appArgs)) {
      appCtx.getBean(DesktopManager.class).openBrowser();
    }
  }

  private static boolean needsOpenBrowser(ApplicationArguments appArgs) {
    String openValue = SitCvApplicationOption.OPEN_BROWSER.getValue(appArgs, "true");
    return !openValue.equals("false");
  }

  /**
   * Initialize the service before the screen is reloaded by LiveReload of spring-boot-devtools. The
   * screen reload process is triggered when ContextRefreshedEvent is notified.
   *
   * @see org.springframework.boot.devtools.autoconfigure.LocalDevToolsAutoConfiguration
   */
  @PostConstruct
  public void initialize() {
    appCtx.getBean(ServiceFactory.class).initialize();
  }
}
