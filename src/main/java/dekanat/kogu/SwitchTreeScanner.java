package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.util.Context;

public class SwitchTreeScanner extends TreeScanner {
  private Context context;

  public SwitchTreeScanner(Context context) {
    this.context = context;
  }

  @Override
  public Object visitVariable(VariableTree node, Object o) {
    System.out.println("Variable found");
    System.out.println("By name " + node.getName() + " and of type " + node.getType());
    return super.visitVariable(node, o);
  }

  @Override
  public Object visitSwitch(SwitchTree node, Object o) {
    System.out.println("Switch found");
    System.out.println(node.getExpression());
    System.out.println(node.getCases());
    return super.visitSwitch(node, o);
  }
}
