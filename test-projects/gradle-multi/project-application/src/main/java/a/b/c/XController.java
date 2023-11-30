package a.b.c;

public class XController {
  private XService xService = new XService();

  public void storeData(XEntity xEntity) {
    xService.store(xEntity);
  }
}
