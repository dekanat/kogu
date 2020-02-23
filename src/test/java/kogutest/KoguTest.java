package kogutest;

import javax.tools.Diagnostic;

public class KoguTest {
  {
    Diagnostic.Kind k = Diagnostic.Kind.ERROR;

    E e = E.INSTANCE;
    TE te = TE.INSTANCE2;
    final String var = "var";

    switch (k) {
      case ERROR:
        System.out.println("Instance");
        break;
    }

    switch (e) {
      case INSTANCE:
        System.out.println("Instance");
        break;
      case ANOTHER_INSTANCE:
        System.out.println("Another instance");
        break;
    }

    switch (e) {
      case INSTANCE:
        System.out.println("Instance");
        break;
    }

    switch (te) {
      case INSTANCE:
        System.out.println("Instance");
        break;
    }
  }
}

enum E {
  INSTANCE,
  ANOTHER_INSTANCE;
  String p;
}
