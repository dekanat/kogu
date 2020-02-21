package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.util.Context;

public class ASTScanner extends TreeScanner {
  private Context context;
  private State state = State.INSTANCE;

  public ASTScanner(Context context) {
    this.context = context;
  }

  @Override
  public Object visitVariable(VariableTree node, Object o) {
    state.addVariableDeclaration(new EnumDeclaration(node));
    return super.visitVariable(node, o);
  }

  @Override
  public Object visitSwitch(SwitchTree node, Object o) {
    state.addSwitch(new Switch(node));
    return super.visitSwitch(node, o);
  }
}
