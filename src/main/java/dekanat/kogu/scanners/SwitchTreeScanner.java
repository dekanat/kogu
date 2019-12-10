package dekanat.kogu.scanners;

import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

public class SwitchTreeScanner extends TreeScanner {
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
