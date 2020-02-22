package kogutest;

public class KoguTest {
  {
    E instance = E.INSTANCE;
    final String var = "var";

    switch (instance) {
      case INSTANCE:
        System.out.println("Instance");
        break;
      default:
        System.out.println("default");
    }
  }
}

enum E {
  INSTANCE,
  ANOTHER_INSTANCE;
  String p;
}
