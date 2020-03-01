package dekanat.kogu.logging;

import java.util.ListResourceBundle;

public class KoguMessages extends ListResourceBundle {
  private static final String ERROR_PREFIX = "compiler.err.";
  private static final String WARNING_PREFIX = "compiler.warn.";

  public static final String INEXHAUSTIVE_MATCH = "kogu.inexhaustive_match";

  private final StringBuilder inexhaustiveMatchMessage = new StringBuilder();
  {
    inexhaustiveMatchMessage
      .append("Inexhaustive match detected over enum {0}")
      .append(System.lineSeparator())
      .append("You are missing the following members [ {1} ].")
      .append(" You may also fix this just by adding the \"default\" case")
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
}
