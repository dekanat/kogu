package dekanat.kogu;

import java.util.*;

enum State {
  INSTANCE;

  private final static Set<EnumSwitch> partialSwitches = new HashSet<>();
  // This serves as a cache. If the enum is not found here, we go to package, named and starred
  // scopes to resolve it
  private final static Map<String, EnumDefinition> enumDefinitions = new HashMap<>();

  private static Map<String, List<EnumSwitch>> switches = new HashMap<>();

  public static State rinsed() {
    switches = new HashMap<>();
    return INSTANCE;
  }

  public void addEnumDefinition(EnumDefinition enumDefinition) {
    enumDefinitions.put(enumDefinition.fullName, enumDefinition);
  }

  public void addSwitch(EnumSwitch theSwitch) {
    List<EnumSwitch> enumSwitches = switches.get(theSwitch.subjectType);
    if (enumSwitches == null) {
      switches.put(theSwitch.subjectType, new ArrayList<EnumSwitch>(){{ this.add(theSwitch); }});
    } else {
      enumSwitches.add(theSwitch);
    }
  }

  public Report evaluate() {
    switches.values().stream().flatMap(Collection::stream).forEach(enumSwitch -> {
      EnumDefinition enumDefinition = enumDefinitions.get(enumSwitch.subjectType);

      if (!enumSwitch.hasDefault() && enumDefinition != null) {
        if (!enumSwitch.cases.containsAll(enumDefinition.instanceMembers)) {
          partialSwitches.add(enumSwitch);
        }
      }
    });

    return new Report(partialSwitches);
  }

  public void brief() {
    System.out.println("****** Brief description");
    System.out.println();
    System.out.println("* Switches");
    switches.values().stream().flatMap(Collection::stream).forEach(v -> System.out.println(v));
    System.out.println();
    System.out.println("* Enums");
    enumDefinitions.forEach((k, v) -> System.out.println(v));

  }

  public void verbose() {
    System.out.println("****** Verbose description");
  }
}
