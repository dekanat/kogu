package dekanat.kogu;

public class Position {
  public final int lineNumber;
  public final int colNumber;

  public Position(int lineNumber, int colNumber) {
    this.lineNumber = lineNumber;
    this.colNumber = colNumber;
  }

  @Override
  public String toString() {
    return lineNumber + "," + colNumber;
  }
}
