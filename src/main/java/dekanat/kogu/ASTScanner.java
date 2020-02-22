package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

public class ASTScanner extends TreeScanner {
  private Context context;

  public ASTScanner(Context context) {
    this.context = context;
  }

  @Override
  public Object visitVariable(VariableTree node, Object o) {
    State state = (State) o;
    Symbol symbol = ((JCTree.JCIdent) ((JCTree.JCVariableDecl) node).vartype).sym;
    String supertype = ((Type.ClassType) symbol.type).supertype_field.tsym.name.toString();


    if (supertype.equals("Enum")) {
      state.addVariableDeclaration(new EnumDeclaration(node));
    }

    return super.visitVariable(node, o);
  }

  @Override
  public Object visitSwitch(SwitchTree node, Object o) {
    State state = (State) o;
    JCTree.JCIdent identifier = (JCTree.JCIdent) ((JCTree.JCParens) node.getExpression()).getExpression();
    Type supertype = ((Type.ClassType) identifier.type).supertype_field;
    String supertypeName = supertype.tsym.name.toString();

    if (supertypeName.equals("Enum")) {
      state.addSwitch(new EnumSwitch(node));
    }

    return super.visitSwitch(node, o);
  }
}
