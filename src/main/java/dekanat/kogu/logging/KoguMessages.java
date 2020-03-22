package dekanat.kogu.logging;

import java.util.ListResourceBundle;

public class KoguMessages extends ListResourceBundle {
  public static final String ERROR_PREFIX = "compiler.err.";
  public static final String WARNING_PREFIX = "compiler.warn.";

  public static final String INEXHAUSTIVE_MATCH = "kogu.inexhaustive_match";

  private final StringBuilder inexhaustiveMatchMessage = new StringBuilder();
  {
    inexhaustiveMatchMessage
      .append("{0}: Inexhaustive match detected over enum {1}")
      .append(System.lineSeparator())
      .append(System.lineSeparator())
      .append("  {2}")
      .append(System.lineSeparator())
      .append("  {3}")
      .append(System.lineSeparator())
      .append("  You are missing the following members [ {4} ].")
      .append(" You may also fix this by introducing the \"default\" case")
      .append(System.lineSeparator())
      .append(System.lineSeparator());
  }

  @Override
  protected Object[][] getContents() {
    return new Object[][]{
      {ERROR_PREFIX + INEXHAUSTIVE_MATCH, inexhaustiveMatchMessage.toString()},
      {WARNING_PREFIX + INEXHAUSTIVE_MATCH, inexhaustiveMatchMessage.toString()},
    };
  }

  public String getFormat(String prefix, String key) {
    for (Object[] message : getContents()) {
        if (message[0].equals(prefix + key)) {
            return (String) message[1];
        }
    }

    throw new IllegalArgumentException("No message found for prefix " + prefix + " and key " + key);
  }
}
