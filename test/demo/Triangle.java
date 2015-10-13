package demo;

public class Triangle {
  public static void main(String[] args) {
    log("Hello triangular world!");
    log("hypotenuse(3,4)=" + hypotenuse(3, 4));
    log("hypotenuse(5,12)=" + hypotenuse(5, 12));
  }
  
  private static void log(String message) {
    System.out.println(message);
  }
  
  private static double hypotenuse(double a, double b) {
    return rootSquare(square(a) + square(b));
  }

  private static double rootSquare(double cc) {
    return Math.sqrt(cc);
  }

  private static double square(double a) {
    return a*a;
  }
}
