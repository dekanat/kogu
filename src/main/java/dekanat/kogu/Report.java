package dekanat.kogu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {
  private final Map<String, List<EnumSwitch>> partialSwitches = new HashMap<>();
  private final StringBuilder report = new StringBuilder().append("Kogu report\n\n");

  public Report(Map<String, List<EnumSwitch>> partialSwitches) {
    this.partialSwitches.putAll(partialSwitches);
  }

  public void brief() {
    if (partialSwitches.isEmpty()) {
      report.append("All switches over Enums are exhaustive");
    } else {
      report.append("Partial switches:\n");

      for (List<EnumSwitch> partialSwitches : partialSwitches.values()) {
        for (EnumSwitch enumSwitch: partialSwitches) {
          report.append("  " + enumSwitch.subjectType + "\n");
        }
      }
    }

    System.out.println(report);
  }

  public void verbose() {
    System.out.println("****** Verbose report");
  }
}
