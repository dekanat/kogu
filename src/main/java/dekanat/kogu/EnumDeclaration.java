package dekanat.kogu;

import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.tree.JCTree;

import java.util.Objects;

public class EnumDeclaration {
  public final String name;
  public final String type;

  public EnumDeclaration(VariableTree node) {
    name = node.getName().toString();
    type = ((JCTree.JCVariableDecl) node).vartype.type.toString();
  }

  @Override
  public String toString() {
    StringBuilder stringRep = new StringBuilder()
      .append("Name:\n")
      .append("  " + name + "\n")
      .append("Type:\n")
      .append("  " + type + "\n");

    return stringRep.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumDeclaration that = (EnumDeclaration) o;
    return name.equals(that.name) &&
      type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }
}
