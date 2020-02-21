package dekanat.kogu;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import static com.sun.source.util.TaskEvent.Kind.ANALYZE;

public class KoguListener implements TaskListener {
  private Context context;
  private final State state = State.rinse();

  public KoguListener(Context context) {
    this.context = context;
  }

  @Override
  public void finished(TaskEvent e) {
    if (ANALYZE.equals(e.getKind())) {
      CompilationUnitTree cu = e.getCompilationUnit();

      scanSymbols((JCTree.JCCompilationUnit) cu);
      scanAST(cu);

      state.brief();

      Report report = state.evaluate();
      report.brief();
    }
  }

  private void scanAST(CompilationUnitTree cu) {
    ASTScanner switchVisitor = new ASTScanner(context);
    switchVisitor.scan(cu, null);
  }

  private void scanSymbols(JCTree.JCCompilationUnit cu) {
    Symbol.PackageSymbol ps = cu.packge;
    Iterable<Symbol> packageSymbols = ps.members_field.getSymbols();

    packageSymbols.forEach(this::markIfEnum);
  }

  private void markIfEnum(Symbol s) {

    Type supertype = ((Type.ClassType) s.type).supertype_field;
    String supertypeName = supertype.tsym.name.toString();

    if (supertypeName.equals("Enum")) {
      state.addEnumDefinition(new EnumDefinition(s));
    }
  }
}
