package io.sitoolkit.cv.plugin.maven;

import io.sitoolkit.cv.app.SitCvApplication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "run")
public class RunApplicationMojo extends AbstractMojo {

  public static final String ANALYZE_SQL_OPTION = "analyze-sql";

  private static final String OPEN_BROWSER_OPTION = "open";

  public static final String SPECIFY_TEST_OPTION = "test";

  public static final String APP_OPTION_PREFIX = "--cv.";

  @Parameter(property = ANALYZE_SQL_OPTION, defaultValue = "false")
  private boolean analyzeSql;

  @Parameter(property = OPEN_BROWSER_OPTION, defaultValue = "true")
  private boolean openBrowser;

  @Parameter(property = SPECIFY_TEST_OPTION, defaultValue = "")
  private String specifyTest;

  @Parameter private String cvArgs;

  @Parameter(defaultValue = "x")
  private String stopKey;

  @Override
  public void execute() throws MojoExecutionException {

    SitCvApplication.main(getArgsAsArray());

    getLog().info("Press " + stopKey + " and enter to stop server");

    if (StringUtils.isNotEmpty(stopKey)) {
      try (Scanner scanner = new Scanner(System.in)) {
        while (true) {
          if (StringUtils.equalsIgnoreCase(stopKey, scanner.nextLine())) {
            break;
          }
        }
      }
    }
  }

  private String[] getArgsAsArray() {
    String[] cvArgsArray = StringUtils.defaultString(cvArgs).split(" ");
    List<String> args = new ArrayList<>(Arrays.asList(cvArgsArray));

    if (analyzeSql) {
      args.add(APP_OPTION_PREFIX + ANALYZE_SQL_OPTION);
    }

    if (!openBrowser) {
      args.add(APP_OPTION_PREFIX + OPEN_BROWSER_OPTION + "=false");
    }

    if (StringUtils.isNotBlank(specifyTest)) {
      args.add(APP_OPTION_PREFIX + SPECIFY_TEST_OPTION + "=" + specifyTest);
    }

    return args.toArray(new String[] {});
  }
}
