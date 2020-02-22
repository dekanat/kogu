package dekanat.kogu;

import com.sun.source.tree.SwitchTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EnumSwitch {
  public final String identifierName;
  public final String identifierType;
  private boolean hasDefault = false;
  public final Set<String> cases = new HashSet<>();

  public EnumSwitch(SwitchTree switchTree) {
    JCTree.JCIdent identifier = (JCTree.JCIdent) ((JCTree.JCParens) switchTree.getExpression()).getExpression();
    identifierName = identifier.name.toString();
    identifierType = identifier.type.toString();
    List<JCTree.JCCase> switchCases = ((JCTree.JCSwitch) switchTree).getCases();

    for (JCTree.JCCase cs: switchCases) {
      if (cs.pat != null) {
        cases.add(((JCTree.JCIdent) cs.pat).name.toString());
      } else {
        hasDefault = true;
      }
    }
  }

  public boolean hasDefault() {
    return hasDefault;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumSwitch aSwitch = (EnumSwitch) o;
    return identifierName.equals(aSwitch.identifierName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifierName);
  }

  @Override
  public String toString() {
    StringBuilder stringRep = new StringBuilder()
      .append("Identifier:\n")
      .append("  named " + identifierName + " and of type " + identifierType + "\n")
      .append("Cases:\n")
      .append("  [\n");

    for (String cs : cases) {
      stringRep.append("    " + cs + "\n");
    }

    stringRep
      .append("  ]\n")
      .append("Has default:\n")
      .append("    " + hasDefault + "\n");

    return stringRep.toString();
  }
}
