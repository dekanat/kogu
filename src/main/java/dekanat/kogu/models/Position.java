package dekanat.kogu.models;

public class Position {
  public final int lineNumber;
  public final int colNumber;
  public final String line;

  public Position(String line, int lineNumber, int colNumber) {
    this.line = line;
    this.lineNumber = lineNumber;
    this.colNumber = colNumber;
  }

  @Override
  public String toString() {
    return lineNumber + "," + colNumber;
  }
}
