package kogutest.other;

import kogutest.Host;
import kogutest.KoguTest;
import kogutest.TE;

public class OtherHost {
  public enum HE {
    HEI1,
    HEI2;
  }

  {
    Host.HE hei1 = Host.HE.HEI1;

    switch (hei1) {
      case HEI1:
        System.out.println("HEI1");
    }

    switch (TE.INSTANCE) {
      case INSTANCE2:
        System.out.println("INSTANCE2");
    }

    switch (KoguTest.HEz.HEIz1) {
      case HEIz1:
        System.out.println("HEIz1");
    }
  }
}
