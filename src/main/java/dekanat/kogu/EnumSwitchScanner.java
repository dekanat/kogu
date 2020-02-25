package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Type.ClassType;
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
    JCTree.JCExpression switchExpression = ((JCTree.JCParens) node.getExpression()).getExpression();
    ClassType subjectType = null;

    if (switchExpression instanceof JCTree.JCFieldAccess) {
      subjectType = ((ClassType)((ClassType) switchExpression.type.tsym.type).supertype_field);
    } else if (switchExpression instanceof JCTree.JCIdent) {
      subjectType = (ClassType) ((ClassType) switchExpression.type).supertype_field;
    }

    if (subjectType != null) {
      String subjectSupertypeName = subjectType.tsym.getQualifiedName().toString();

      if (subjectSupertypeName.equals("java.lang.Enum")) {
        String subjectTypeName = subjectType.typarams_field.get(0).toString();
        state.addSwitch(new EnumSwitch(node, subjectTypeName));
      }
    }

    return super.visitSwitch(node, o);
  }
}
