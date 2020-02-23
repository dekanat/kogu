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
  public KoguListener(Context context) {
    this.context = context;
  }


  @Override
  public void finished(TaskEvent e) {
    if (ANALYZE.equals(e.getKind())) {
      final State state = State.rinsed();

      CompilationUnitTree cu = e.getCompilationUnit();

      scanSymbols((JCTree.JCCompilationUnit) cu, state);
      scanAST(cu, state);

      Report report = state.evaluate();
      report.brief();
    }
  }

  private void scanAST(CompilationUnitTree cu, State state) {
    EnumSwitchScanner switchVisitor = new EnumSwitchScanner(context);
    switchVisitor.scan(cu, state);
  }

  private void scanSymbols(JCTree.JCCompilationUnit cu, State state) {
    Symbol.PackageSymbol ps = cu.packge;
    Iterable<Symbol> packageSymbols = ps.members_field.getSymbols();

    for (Symbol symbol : packageSymbols) {
      markIfEnum(symbol, state);
    }
  }

  private void markIfEnum(Symbol s, State state) {

    Type supertype = ((Type.ClassType) s.type).supertype_field;
    String supertypeName = supertype.tsym.name.toString();

    if (supertypeName.equals("Enum")) {
      state.addEnumDefinition(new EnumDefinition(s));
    }
  }
}
