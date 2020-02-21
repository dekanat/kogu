package dekanat.kogu;

import com.sun.source.tree.VariableTree;

import java.util.Objects;

public class EnumDeclaration {
  private String name;
  private String type;

  public EnumDeclaration(VariableTree node) {
    name = node.getName().toString();
    type = node.getType().toString();
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "name = '" + name + '\'' + "type = '" + type + '\'';
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
