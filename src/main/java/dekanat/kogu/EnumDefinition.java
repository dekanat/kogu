package dekanat.kogu;

import com.sun.tools.javac.code.Symbol;

import java.util.Objects;

public class EnumDefinition {
  private String name;

  public EnumDefinition(Symbol symbol) {
    name = symbol.name.toString();
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumDefinition that = (EnumDefinition) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
