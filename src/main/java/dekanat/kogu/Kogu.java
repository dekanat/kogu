package dekanat.kogu;

import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.util.Context;


public class Kogu implements Plugin {

  @Override
  public String getName() {
    return "Kogu";
  }

  @Override
  public void init(JavacTask javacTask, String... strings) {
    Context context = ((BasicJavacTask) javacTask).getContext();
    javacTask.addTaskListener(new KoguListener(context));
  }
}