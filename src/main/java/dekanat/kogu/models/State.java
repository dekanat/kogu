package dekanat.kogu.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.text.MessageFormat;

import com.sun.tools.javac.util.Log;

import dekanat.kogu.logging.KoguMessages;

public enum State {
  INSTANCE;

  private final static Map<String, List<EnumSwitch>> allPartialSwitches = new HashMap<>();
  private static Map<String, List<EnumSwitch>> localPartialSwitches = new HashMap<>();
  private static Map<String, List<EnumSwitch>> switches = new HashMap<>();

  // This serves as a cache. If the enum is not found here, we go to package, named and starred
  // scopes to resolve it
  private final static Map<String, EnumDefinition> enumDefinitions = new HashMap<>();
  private static String fileName;

  public static State rinsed() {
    allPartialSwitches.putAll(localPartialSwitches);
    localPartialSwitches = new HashMap<>();
    switches = new HashMap<>();
    fileName = null;
    return INSTANCE;
  }

  public void setFilename(String _fileName) {
    fileName = _fileName;
  }

  public void addEnumDefinition(EnumDefinition enumDefinition) {
    enumDefinitions.put(enumDefinition.fullName, enumDefinition);
  }

  public boolean isEnumTypeResolved(final String enumType) {
    return enumDefinitions.get(enumType) != null;
  }

  public void addSwitch(EnumSwitch theSwitch) {
    List<EnumSwitch> enumSwitches = switches.get(theSwitch.subjectType);
    if (enumSwitches == null) {
      switches.put(theSwitch.subjectType, new ArrayList<EnumSwitch>() {{
        this.add(theSwitch);
      }});
    } else {
      enumSwitches.add(theSwitch);
    }
  }

  public Set<String> getSwitchEnumTypes() {
    return switches.keySet();
  }

  public void evaluate() {
    switches.values().stream().flatMap(Collection::stream).forEach(enumSwitch -> {
      EnumDefinition enumDefinition = enumDefinitions.get(enumSwitch.subjectType);

      if (!enumSwitch.hasDefault() && enumDefinition != null) {
        if (!enumSwitch.cases.containsAll(enumDefinition.instanceMembers)) {
          addPartialSwitch(enumSwitch);
        }
      }
    });
  }

  public void flushVia(final Log logger, boolean strictReporting) {
    for (Map.Entry<String, List<EnumSwitch>> enumSwitchEntries : localPartialSwitches.entrySet()) {
      for (EnumSwitch enumSwitch : enumSwitchEntries.getValue()) {
        List<EnumSwitch> enumSwitchesForCU = allPartialSwitches.get(enumSwitchEntries.getKey());

        if(enumSwitchesForCU == null ||
          (enumSwitchesForCU != null && !enumSwitchesForCU.contains(enumSwitch))) {
          Set<String> enumMembers = enumDefinitions.get(enumSwitch.subjectType).instanceMembers;

          String missingCases = enumMembers.stream()
            .filter(m -> !enumSwitch.cases.contains(m))
            .reduce((l, r) -> l + ", " + r)
            .get();

          Position switchPositionInFile = resolvePositionInFile(enumSwitch, fileName);

          String markerLine = spaces(switchPositionInFile.colNumber) + "^";

          String reportType = strictReporting ? "error" : "warning";

          Object[] params = new Object[]{
            reportType,
            enumSwitch.subjectType, 
            switchPositionInFile.line, 
            markerLine, 
            missingCases
          };
          
          String errorTemplate = new KoguMessages().getFormat(KoguMessages.ERROR_PREFIX, KoguMessages.INEXHAUSTIVE_MATCH);

          // The code below is (I suppose) a dirty hack but it works. Somehow Java 8 won't:
          //  1. register a resource bundle
          //  2. won't print the error lines in raw error
          // so I have to print raw lines and then report an error with an empty string
          logger.printRawLines(fileName + ":" + switchPositionInFile);
          logger.printRawLines(MessageFormat.format(errorTemplate, params));
          
          if (strictReporting) {
            logger.rawError(enumSwitch.position.getPreferredPosition(), "");
          } else  {
            logger.rawWarning(enumSwitch.position.getPreferredPosition(), "");
          }
        }
      }
    }
  }

  private String spaces(int n) {
    StringBuffer spaces = new StringBuffer(n);
    for (int i = 0; i < n; i++){
      spaces.append(" ");
    }
    return spaces.toString();
  }

  private Position resolvePositionInFile(EnumSwitch enumSwitch, String fileName) {
    boolean expressionFound = false;
    int switchLineNumber = 0;
    int switchColumnNumber = 0;
    int linesRead = 0;
    String switchLine = "";

    try (Scanner scanner = new Scanner(new File(fileName))) {
      while (scanner.hasNextLine()) {
        String lineAtHand = scanner.nextLine();
        linesRead++;

        if (lineAtHand.contains("switch")) {
          switchLineNumber = linesRead;
          switchColumnNumber = lineAtHand.lastIndexOf("switch");
          switchLine = lineAtHand;
        }

        if (lineAtHand.trim().contains(enumSwitch.subjectExpression.trim())) {
          expressionFound = true;
        }

        if (expressionFound){
          break;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return new Position(switchLine, switchLineNumber, switchColumnNumber);
  }

  private void addPartialSwitch(EnumSwitch partialSwitch) {
    List<EnumSwitch> enumSwitches = localPartialSwitches.get(partialSwitch.subjectType);
    if (enumSwitches == null) {
      localPartialSwitches.put(partialSwitch.subjectType, new ArrayList<EnumSwitch>() {{
        this.add(partialSwitch);
      }});
    } else {
      enumSwitches.add(partialSwitch);
    }
  }

  public void print() {
    System.out.println("****** Brief description");
    System.out.println();
    System.out.println("* Switches");
    switches.values().stream().flatMap(Collection::stream).forEach(v -> System.out.println(v));
    System.out.println();
    System.out.println("* All partial switches");
    allPartialSwitches.values().stream().flatMap(Collection::stream).forEach(v -> System.out.println(v));
    System.out.println();
    System.out.println("* Local partial switches");
    localPartialSwitches.values().stream().flatMap(Collection::stream).forEach(v -> System.out.println(v));
    System.out.println();
    System.out.println("* Enums");
    enumDefinitions.forEach((k, v) -> System.out.println(v));

  }

  public boolean containsSwitches() {
    return !switches.isEmpty();
  }
}
