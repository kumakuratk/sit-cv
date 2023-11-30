package a.b.c;

public class XService {
  private XRepository xRepository = new XRepository();

  public void store(XEntity xEntity) {
    xRepository.save(xEntity);
  }
}
