package dekanat.kogu;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacMessages;
import dekanat.kogu.logging.KoguMessages;

import java.util.ResourceBundle;


public class Kogu implements Plugin {

  @Override
  public String getName() {
    return "Kogu";
  }

  @Override
  public void init(JavacTask javacTask, String... strings) {
    Context context = ((BasicJavacTask) javacTask).getContext();

    JavacMessages.instance(context).add(locale ->
      ResourceBundle.getBundle(KoguMessages.class.getName(), locale)
    );

    javacTask.addTaskListener(new KoguListener(context));
  }
}