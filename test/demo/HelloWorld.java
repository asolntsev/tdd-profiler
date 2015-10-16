package demo;

public class HelloWorld implements Runnable {
  public static void main(String[] args) throws InterruptedException {
    new HelloWorld().run();
  }

  public void run() {
    System.out.println("Hello ...");
    System.out.println("World!");
  }
}
