package dekanat.kogu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

enum State {
  INSTANCE;

  private final static Set<EnumSwitch> partialSwitches = new HashSet<>();
  private final static Map<String, EnumDefinition> enumDefinitions = new HashMap<>();

  private static Set<EnumSwitch> switches = new HashSet<>();

  public static State rinsed() {
    switches = new HashSet<>();
    return INSTANCE;
  }

  public void addEnumDefinition(EnumDefinition enumDefinition) {
    enumDefinitions.put(enumDefinition.fullName, enumDefinition);
  }

  public void addSwitch(EnumSwitch theSwitch) {
    switches.add(theSwitch);
  }

  public Report evaluate() {

    for (EnumSwitch enumSwitch : switches) {
      EnumDefinition enumDefinition = enumDefinitions.get(enumSwitch.identifierType);

      if (!enumSwitch.hasDefault() && enumDefinition != null) {
        if (!enumSwitch.cases.containsAll(enumDefinition.instanceMembers)) {
          partialSwitches.add(enumSwitch);
        }
      }
    }

    return new Report(partialSwitches);
  }

  public void brief() {
    System.out.println("****** Brief description");
    System.out.println();
    System.out.println("* Switches");
    switches.forEach(s -> System.out.println(s));
    System.out.println();
    System.out.println("* Enums");
    enumDefinitions.forEach((k, v) -> System.out.println(v));

  }

  public void verbose() {
    System.out.println("****** Verbose description");
  }
}
