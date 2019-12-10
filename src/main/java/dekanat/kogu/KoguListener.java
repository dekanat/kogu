package dekanat.kogu;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import dekanat.kogu.scanners.AnalyzeTreeScanner;
import dekanat.kogu.scanners.IdentifierCounter;
import dekanat.kogu.scanners.SwitchTreeScanner;

import static com.sun.source.util.TaskEvent.Kind.ANALYZE;
import static com.sun.source.util.TaskEvent.Kind.PARSE;

public class KoguListener implements TaskListener {
  private Context context;
  public KoguListener(Context context) {
    this.context = context;
  }

  @Override
  public void started(TaskEvent e) {
  }

  @Override
  public void finished(TaskEvent e) {
    if (PARSE.equals(e.getKind())) {
      TreeScanner switchVisitor = new SwitchTreeScanner(context);
      JCTree cu = (JCTree) e.getCompilationUnit();
      switchVisitor.scan(cu, null);

      Types types = Types.instance(context);
//          System.out.println(Names.instance(context).);

      IdentifierCounter counter = new IdentifierCounter(context);
      Integer scan = counter.scan(cu, null);
      System.out.println("Scan " + scan);

//          CompilationUnitTree cu = e.getCompilationUnit();
//          System.out.println(cu);
    } else if (ANALYZE.equals(e.getKind())) {
      AnalyzeTreeScanner analyzeTreeScanner = new AnalyzeTreeScanner(context);
      JCTree compilationUnit = (JCTree) e.getCompilationUnit();
      analyzeTreeScanner.scan(compilationUnit, null);
    }
  }
}

