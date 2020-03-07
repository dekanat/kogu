package kogutest;

public class SelfContained {
  {
    SE se1 = SE.SE1;

    switch (se1) {
      case SE1:
        System.out.println("In SE1");
        break;
    }
  }
}

enum SE {
  SE1, SE2;
}
