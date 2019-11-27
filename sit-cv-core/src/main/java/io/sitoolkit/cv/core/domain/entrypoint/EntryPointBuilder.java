package io.sitoolkit.cv.core.domain.entrypoint;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EntryPointBuilder {

  public SortedSet<EntryPoint> build(List<String> allIds) {
    SortedSet<EntryPoint> entryPoints = new TreeSet<>(Comparator.comparing(EntryPoint::getId));
    entryPoints.addAll(allIds.stream().map(EntryPoint::of).collect(Collectors.toList()));
    return entryPoints;
  }
}
