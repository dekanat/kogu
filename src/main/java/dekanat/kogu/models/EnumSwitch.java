package dekanat.kogu.models;

import com.sun.source.tree.SwitchTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.List;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EnumSwitch {
  public final String subjectType;
  public final String subjectExpression;
  public final Set<String> cases = new HashSet<>();
  private boolean hasDefault = false;
  public final JCDiagnostic.DiagnosticPosition position;

  public EnumSwitch(SwitchTree switchTree, String subjectType) {
    this.subjectType = subjectType;
    this.subjectExpression = switchTree.getExpression().toString();
    List<JCTree.JCCase> switchCases = ((JCTree.JCSwitch) switchTree).getCases();

    for (JCTree.JCCase cs : switchCases) {
      if (cs.pat != null) {
        cases.add(((JCTree.JCIdent) cs.pat).name.toString());
      } else {
        hasDefault = true;
      }
    }

    this.position = ((JCTree.JCSwitch) switchTree).pos();
  }

  public boolean hasDefault() {
    return hasDefault;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumSwitch that = (EnumSwitch) o;
    return hasDefault == that.hasDefault &&
      subjectType.equals(that.subjectType) &&
      subjectExpression.equals(that.subjectExpression) &&
      cases.equals(that.cases) &&
      position.equals(that.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subjectType, subjectExpression, cases, hasDefault, position);
  }

  @Override
  public String toString() {
    StringBuilder stringRep = new StringBuilder()
      .append("Expression:\n")
      .append("  " + subjectExpression + "\n")
      .append("Type:\n")
      .append("  " + subjectType + "\n")
      .append("Cases:\n")
      .append("  [\n")
      .append(cases.stream()
        .map(c -> "    " + c + "\n")
        .reduce((l, r) -> l + r)
        .get())
      .append("  ]\n")
      .append("Provides default case:\n")
      .append("  " + hasDefault + "\n");

    return stringRep.toString();
  }
}
