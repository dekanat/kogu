package dekanat.kogu;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Names;

import static com.sun.source.util.TaskEvent.Kind.ANALYZE;
import static com.sun.source.util.TaskEvent.Kind.PARSE;

class SwitchTreeScanner extends TreeScanner {
  private Context context;

  public SwitchTreeScanner(Context context) {
    this.context = context;
  }

//  @Override
//  public Object visitIdentifier(IdentifierTree node, Object o) {
//    System.out.println("Identifier visited - " + node.getName());
//    return super.visitIdentifier(node, o);
//  }

  @Override
  public Object visitVariable(VariableTree node, Object o) {
    System.out.println("Variable visited: name - " + node.getName() + " type - " + ((JCTree.JCVariableDecl) node).vartype);
    return super.visitVariable(node, o);
  }

  @Override
  public Object visitSwitch(SwitchTree node, Object o) {
    System.out.println(node.getCases());
    System.out.println("vs " + ((JCTree.JCIdent) ((JCTree.JCParens) node.getExpression()).getExpression()).getKind());
    return super.visitSwitch(node, o);
  }
}

class AnalyzeTreeScanner extends TreeScanner<Void, Void> {
  private final Context context;

  private final Names names;
  private final Log log;
  private final Symtab syms;
  private final Types types;
  private final Check chk;
  private TreeMaker make;
  private final Resolve rs;
  private final JCDiagnostic.Factory diags;
  private Env<AttrContext> attrEnv;
  private Lint lint;

  public AnalyzeTreeScanner(Context context) {
    this.context = context;
    names = Names.instance(context);
    log = Log.instance(context);
    syms = Symtab.instance(context);
    types = Types.instance(context);
    chk = Check.instance(context);
    lint = Lint.instance(context);
    rs = Resolve.instance(context);
    diags = JCDiagnostic.Factory.instance(context);
    Source source = Source.instance(context);
  }

  @Override
  public Void visitSwitch(SwitchTree node, Void aVoid) {
    System.out.println(node);
//    System.out.println(names.table);
    return super.visitSwitch(node, aVoid);
  }
}

class IdentifierCounter extends TreeScanner<Integer, Void> {
  private Context context;

  public IdentifierCounter(Context context) {
    this.context = context;
  }

  @Override
  public Integer visitVariable(VariableTree node, Void aVoid) {
    Types iTypes = Types.instance(context);
    Names iNames = Names.instance(context);
    TypeAnnotations iTypeAnnotations = TypeAnnotations.instance(context);

//    System.out.println("name " + node.getName() +
//      " initializer " + node.getInitializer() +
//      " type " + node.getType() +
//      " isEnum " + ((JCTree)node.getType()).getKind()
//    );

    Symbol symbol = TreeInfo.symbolFor((JCTree) node.getInitializer());
//    System.out.println("Symbol " + symbol);
    return super.visitVariable(node, aVoid);
  }

  @Override
  public Integer visitIdentifier(IdentifierTree node, Void aVoid) {
    return 1;
  }

  @Override
  public Integer reduce(Integer r1, Integer r2) {
    return (r1 == null ? 0 : r1) + (r2 == null ? 0 : r2);
  }
}

public class Kogu implements Plugin {

  @Override
  public String getName() {
    return "Kogu";
  }

  @Override
  public void init(JavacTask javacTask, String... strings) {
    javacTask.addTaskListener(new TaskListener() {
      @Override
      public void started(TaskEvent e) {

      }

      @Override
      public void finished(TaskEvent e) {
        if (PARSE.equals(e.getKind())) {
          Context context = ((BasicJavacTask) javacTask).getContext();
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
          Context context = ((BasicJavacTask) javacTask).getContext();
          AnalyzeTreeScanner analyzeTreeScanner = new AnalyzeTreeScanner(context);
          JCTree compilationUnit = (JCTree) e.getCompilationUnit();
          analyzeTreeScanner.scan(compilationUnit, null);
        }
      }
    });
  }
}