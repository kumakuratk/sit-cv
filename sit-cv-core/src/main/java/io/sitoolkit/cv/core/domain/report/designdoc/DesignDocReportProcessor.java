package io.sitoolkit.cv.core.domain.report.designdoc;

import java.util.List;
import java.util.SortedSet;

import io.sitoolkit.cv.core.domain.entrypoint.EntryPoint;
import io.sitoolkit.cv.core.domain.menu.MenuItem;
import io.sitoolkit.cv.core.domain.report.Report;

public class DesignDocReportProcessor {

  public Report<?> process(List<MenuItem> menuList) {
    return Report.builder().path("assets/designdoc-list.js").content(menuList).build();
  }

  public Report<?> processEntryPoint(SortedSet<EntryPoint> entryPointSet) {
    return Report.builder().path("assets/designdoc-entrypoint.js").content(entryPointSet).build();
  }
}
