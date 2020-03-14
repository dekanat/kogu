package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree;
import dekanat.kogu.models.EnumSwitch;
import dekanat.kogu.models.State;

public class EnumSwitchScanner extends TreeScanner<Object, State> {
  @Override
  public Object visitSwitch(final SwitchTree node, final State state) {
    final JCTree.JCExpression switchExpression = ((JCTree.JCParens) node.getExpression()).getExpression();
    ClassType subjectType = null;

    if (switchExpression instanceof JCTree.JCFieldAccess) {
      subjectType = ((ClassType)((ClassType) switchExpression.type.tsym.type).supertype_field);
    } else if (switchExpression instanceof JCTree.JCIdent) {
      subjectType = (ClassType) ((ClassType) switchExpression.type).supertype_field;
    }

    if (subjectType != null) {
      final String subjectSupertypeName = subjectType.tsym.toString();

      if (subjectSupertypeName.equals("java.lang.Enum")) {
        final String subjectTypeName = subjectType.typarams_field.get(0).toString();
        state.addSwitch(new EnumSwitch(node, subjectTypeName));
      }
    }

    return super.visitSwitch(node, state);
  }
}
