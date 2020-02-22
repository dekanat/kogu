package dekanat.kogu;

import java.util.HashSet;
import java.util.Set;

enum State {
  INSTANCE;

  private final static Set<EnumSwitch> partialSwitches = new HashSet<>();
  private final static Set<EnumDefinition> enumDefinitions = new HashSet<>();

  private static Set<EnumDeclaration> enumDeclarations = new HashSet<>();
  private static Set<EnumSwitch> switches = new HashSet<>();

  public static State rinse() {
    enumDeclarations = new HashSet<>();
    switches = new HashSet<>();
    return INSTANCE;
  }

  public static Set<EnumDeclaration> getEnumDeclarations() {
    return enumDeclarations;
  }

  public static Set<EnumDefinition> getEnumDefinitions() {
    return enumDefinitions;
  }

  public static Set<EnumSwitch> getSwitches() {
    return switches;
  }

  public static Set<EnumSwitch> getPartialSwitches() {
    return partialSwitches;
  }

  public void addVariableDeclaration(EnumDeclaration enumDeclaration) {
    enumDeclarations.add(enumDeclaration);
  }

  public void addEnumDefinition(EnumDefinition enumDefinition) {
    enumDefinitions.add(enumDefinition);
  }

  public void addSwitch(EnumSwitch theSwitch) {
    switches.add(theSwitch);
  }

  public Report evaluate() {
    return new Report();
  }

  public void brief() {
    System.out.println("****** Brief description");
    System.out.println();
    System.out.println("* Switches");
    switches.forEach(s -> System.out.println(s));
    System.out.println();
    System.out.println("* Variables");
    enumDeclarations.forEach(ed -> System.out.println(ed));
    System.out.println();
    System.out.println("* Enums");
    enumDefinitions.forEach(ed -> System.out.println(ed));

  }

  public void verbose() {
    System.out.println("****** Verbose description");
  }
}
