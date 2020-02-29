package dekanat.kogu;

import java.util.*;

enum State {
  INSTANCE;

  private final static Map<String, List<EnumSwitch>> allPartialSwitches = new HashMap<>();
  private static Map<String, List<EnumSwitch>> localPartialSwitches = new HashMap<>();
  private static Map<String, List<EnumSwitch>> switches = new HashMap<>();

  // This serves as a cache. If the enum is not found here, we go to package, named and starred
  // scopes to resolve it
  private final static Map<String, EnumDefinition> enumDefinitions = new HashMap<>();

  public static State rinsed() {
    allPartialSwitches.putAll(localPartialSwitches);
    localPartialSwitches = new HashMap<>();
    switches = new HashMap<>();
    return INSTANCE;
  }

  public void addEnumDefinition(EnumDefinition enumDefinition) {
    enumDefinitions.put(enumDefinition.fullName, enumDefinition);
  }

  public boolean isEnumTypeResolved(String enumType) {
    return enumDefinitions.get(enumType) != null;
  }

  public void addSwitch(EnumSwitch theSwitch) {
    List<EnumSwitch> enumSwitches = switches.get(theSwitch.subjectType);
    if (enumSwitches == null) {
      switches.put(theSwitch.subjectType, new ArrayList<EnumSwitch>(){{ this.add(theSwitch); }});
    } else {
      enumSwitches.add(theSwitch);
    }
  }

  public Set<String> getSwitchEnumTypes() {
    return switches.keySet();
  }

  public Report evaluate() {
    switches.values().stream().flatMap(Collection::stream).forEach(enumSwitch -> {
      EnumDefinition enumDefinition = enumDefinitions.get(enumSwitch.subjectType);

      if (!enumSwitch.hasDefault() && enumDefinition != null) {
        if (!enumSwitch.cases.containsAll(enumDefinition.instanceMembers)) {
          addPartialSwitch(enumSwitch);
        }
      }
    });

    return new Report(localPartialSwitches);
  }

  private void addPartialSwitch(EnumSwitch partialSwitch) {
    List<EnumSwitch> enumSwitches = localPartialSwitches.get(partialSwitch.subjectType);
    if (enumSwitches == null) {
      localPartialSwitches.put(partialSwitch.subjectType, new ArrayList<EnumSwitch>(){{ this.add(partialSwitch); }});
    } else {
      enumSwitches.add(partialSwitch);
    }
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

  public boolean containsSwitches() {
    return !switches.isEmpty();
  }
}
