package dekanat.kogu;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import java.util.HashMap;
import java.util.Map;

import static com.sun.source.util.TaskEvent.Kind.ANALYZE;

public class KoguListener implements TaskListener {
  private Context context;

  public KoguListener(Context context) {
    this.context = context;
  }

  private final Map<String, String> enumOccurrences = new HashMap<>();

  @Override
  public void started(TaskEvent e) {
  }

  @Override
  public void finished(TaskEvent e) {
    if (ANALYZE.equals(e.getKind())) {
      CompilationUnitTree cu = e.getCompilationUnit();
      System.out.println("In ANALYZE " + cu.getSourceFile().getName());

      System.out.println("***** Symbol scanner");
      Symbol.PackageSymbol ps = ((JCTree.JCCompilationUnit)cu).packge;
      Iterable<Symbol> packageSymbols = ps.members_field.getSymbols();

      packageSymbols.forEach(this::markIfEnum);
      enumOccurrences.forEach((k, v) -> System.out.println(k + " of type " + v));

      System.out.println("***** Switch visitor");
      SwitchTreeScanner switchVisitor = new SwitchTreeScanner(context);
      switchVisitor.scan(cu, 33);
    }
  }

  private void markIfEnum(Symbol s) {

    Type supertype = ((Type.ClassType) s.type).supertype_field;
    String supertypeName = supertype.tsym.name.toString();

    if (supertypeName.equals("Enum")) {
      enumOccurrences.put(s.name.toString(), supertypeName);
    }
  }
}
