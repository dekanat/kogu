package kogutest;

public class KoguTest {
  private final Integer instance = 0;
  public final String s = "qerob";
  {
    TEE instance = TEE.INSTANCE;
    switch (instance) {
      case INSTANCE:
        System.out.println("Instance");
        break;
      default:
        System.out.println("default");
    }
  }
}

enum TEE {
  INSTANCE
}