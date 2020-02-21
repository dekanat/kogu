package dekanat.kogu;

import com.sun.source.tree.SwitchTree;

import java.util.Objects;

public class Switch {
  private String expression;

  public Switch(SwitchTree switchTree) {
    expression = switchTree.getExpression().toString();
//    System.out.println(node.getCases());
  }

  public String getExpression() {
    return expression;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Switch aSwitch = (Switch) o;
    return expression.equals(aSwitch.expression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expression);
  }
}
