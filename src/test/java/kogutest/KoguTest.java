package kogutest;

import kogutest.other.OtherHost;

import javax.tools.Diagnostic;

public class KoguTest {
  public enum HEz {
    HEIz1,
    HEIz2;
  }

  {
    Diagnostic.Kind k = Diagnostic.Kind.ERROR;
    Host.HE he = Host.HE.HEI1;
    Host.Inner.HIE hie = Host.Inner.HIE.HIEI1;

    E e = E.INSTANCE;
    TE te = TE.INSTANCE2;
    final String var = "var";

    switch (Diagnostic.Kind.ERROR) {
      case ERROR:
        System.out.println("Instance");
        break;
    }

    switch (k) {
      case ERROR:
        System.out.println("Instance");
        break;
    }

    switch (e) {
      case INSTANCE:
        System.out.println("Instance");
        break;
      case INSTANCE2:
        System.out.println("Instance2");
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

    switch (he) {
      case HEI1:
        System.out.println("Instance");
        break;
    }

    switch (OtherHost.HE.HEI1) {
      case HEI2:
        System.out.println("Instance");
        break;
    }

    switch (hie) {
      case HIEI1:
        System.out.println("Instance");
        break;
    }
  }
}

enum E {
  INSTANCE,
  INSTANCE2;
  String p;
}
