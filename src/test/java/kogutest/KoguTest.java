package kogutest;

public class KoguTest {
  {
    E instance = E.INSTANCE;

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
  INSTANCE
}
