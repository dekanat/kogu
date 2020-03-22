package dekanat.kogu;

import com.sun.tools.javac.util.Options;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.util.Context;


public class Kogu implements Plugin {

  @Override
  public String getName() {
    return "Kogu";
  }

  @Override
  public void init(JavacTask javacTask, String... strings) {
    final Context context = ((BasicJavacTask) javacTask).getContext();

    Options options = Options.instance(context);
    boolean strictReporting = options.get("kogu.strict") != null ? true : false;

    javacTask.addTaskListener(new KoguListener(context, strictReporting));
  }
}