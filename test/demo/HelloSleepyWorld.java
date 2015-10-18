package demo;

public class HelloSleepyWorld implements Runnable {
  public static void main(String[] args) throws InterruptedException {
    new HelloSleepyWorld().run();
  }

  public void run() {
    System.out.println("Hello ...");
    sleep(100);
    slow1();
    sleep(100);
    slow2();
    sleep(100);
    System.out.println("World!");
  }

  private void slow1() {
    sleep(100);
  }

  private void slow2() {
    sleep(600);
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
