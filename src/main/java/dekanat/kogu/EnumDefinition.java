package dekanat.kogu;

import com.sun.tools.javac.code.Symbol;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EnumDefinition {
  public final String fullName;
  public final Set<String> instanceMembers = new HashSet<>();

  public EnumDefinition(Symbol symbol) {
    fullName = symbol.getQualifiedName().toString();

    List<Symbol> enclosedElements = symbol.getEnclosedElements();

    for (Symbol s: enclosedElements) {
      if(s.asType().toString().equals(fullName))
        instanceMembers.add(s.getSimpleName().toString());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumDefinition that = (EnumDefinition) o;
    return fullName.equals(that.fullName) &&
      instanceMembers.equals(that.instanceMembers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, instanceMembers);
  }

  @Override
  public String toString() {
    StringBuilder stringRep = new StringBuilder()
      .append("Full name:\n")
      .append("  " + fullName + "\n")
      .append("Instance members:\n")
      .append("  [\n");

    for (String is : instanceMembers) {
      stringRep.append("    " + is + "\n");
    }

    stringRep
      .append("  ]\n");

    return stringRep.toString();
  }
}
