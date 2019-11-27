package io.sitoolkit.cv.core.domain.entrypoint;

import lombok.Data;

@Data
public class EntryPoint {
  private String id;
  private String url;

  private EntryPoint(String id) {
    this.id = id;
    this.url = parseUrl(id);
  }

  private String parseUrl(String id) {
    return "/designdoc/function/" + id;
  }

  public static EntryPoint of(String id) {
    return new EntryPoint(id);
  }
}
