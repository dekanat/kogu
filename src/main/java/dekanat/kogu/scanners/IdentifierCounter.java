package dekanat.kogu.scanners;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.TypeAnnotations;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

public class IdentifierCounter extends TreeScanner<Integer, Void> {
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
