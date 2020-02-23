package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

public class EnumSwitchScanner extends TreeScanner {
  private Context context;

  public EnumSwitchScanner(Context context) {
    this.context = context;
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
