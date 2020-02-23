package dekanat.kogu;

import java.util.HashSet;
import java.util.Set;

public class Report {
  private final static Set<EnumSwitch> partialSwitches = new HashSet<>();
  private final StringBuilder report = new StringBuilder().append("Kogu report\n\n");

  public Report(Set<EnumSwitch> partialSwitches) {
    this.partialSwitches.addAll(partialSwitches);
  }

  public void brief() {
    if (partialSwitches.isEmpty()) {
      report.append("All switches over Enums are exhaustive");
    } else {
      report.append("Partial switches:\n");

      for (EnumSwitch partialSwitch : partialSwitches) {
        report.append("  " + partialSwitch.location + "\n");
      }
    }

    System.out.println(report);
  }

  public void verbose() {
    System.out.println("****** Verbose report");
  }
}
