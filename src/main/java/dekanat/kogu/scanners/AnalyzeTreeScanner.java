package dekanat.kogu.scanners;

import com.sun.source.tree.SwitchTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Lint;
import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Names;

public class AnalyzeTreeScanner extends TreeScanner<Void, Void> {
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
