package dekanat.kogu;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import dekanat.kogu.models.EnumDefinition;
import dekanat.kogu.models.State;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.sun.source.util.TaskEvent.Kind.ANALYZE;

public class KoguListener implements TaskListener {
  private final Context context;
  private final boolean strictReporting;
  private final String rootFolder = System.getProperty("user.dir") + File.separator;

  public KoguListener(Context context, boolean strictReporting) {
    this.context = context;
    this.strictReporting = strictReporting;
  }

  @Override
  public void started(TaskEvent e) {
  }

  @Override
  public void finished(TaskEvent e) {
    if (ANALYZE.equals(e.getKind())) {
      final CompilationUnitTree cu = e.getCompilationUnit();

      final State state = State.rinsed();
      state.setFilename(rootFolder + cu.getSourceFile().getName());

      scanAST(cu, state);

      if (state.containsSwitches()) {
        resolveEnums(cu, state);
        state.evaluate();
        state.flushVia(Log.instance(context), strictReporting);
      }
    }
  }

  private void resolveEnums(CompilationUnitTree cu, State state) {
    final Set<String> resolvedSwitchEnumTypes = new HashSet<String>();
    final Set<String> unresolvedSwitchEnumTypes = state.getSwitchEnumTypes();

    final Scope packageScope = ((JCTree.JCCompilationUnit) cu).packge.members_field;
    final Scope namedImportScope = ((JCTree.JCCompilationUnit) cu).namedImportScope;
    final Scope starImportScope = ((JCTree.JCCompilationUnit) cu).starImportScope;

    for (String switchEnumType: unresolvedSwitchEnumTypes) {
      boolean isResolved = state.isEnumTypeResolved(switchEnumType);

      isResolved = resolveEnumInScope(packageScope, switchEnumType, state, isResolved);
      isResolved = resolveEnumInScope(namedImportScope, switchEnumType, state, isResolved);
      isResolved = resolveEnumInScope(starImportScope, switchEnumType, state, isResolved);

      if (isResolved) {
        resolvedSwitchEnumTypes.add(switchEnumType);
      }
    }

    if (!resolvedSwitchEnumTypes.containsAll(unresolvedSwitchEnumTypes)) {
      System.err.println("Something went terribly wrong");
    }
  }

  private boolean resolveEnumInScope(Scope scope, String enumType, State state, boolean isResolved) {
    if (!isResolved) {
      final Iterator<Symbol> symbolIterator = scope.getSymbols().iterator();

      while (symbolIterator.hasNext() && !isResolved) {
        final Symbol symbolAtHand = symbolIterator.next();
        final String symbolName = symbolAtHand.getQualifiedName().toString();

        if (enumType.equals(symbolName)) {
          state.addEnumDefinition(new EnumDefinition(symbolAtHand));
          isResolved = true;
        } else if (enumType.startsWith(symbolName)) {
          Scope symbolScope = symbolAtHand.members();
          isResolved = resolveEnumInScope(symbolScope, enumType, state, false);
        }
      }
    }

    return isResolved;
  }

  private void scanAST(CompilationUnitTree cu, State state) {
    final EnumSwitchScanner switchVisitor = new EnumSwitchScanner();
    switchVisitor.scan(cu, state);
  }
}
