package demo;

public class HelloWorldWithThrow implements Runnable {
  public void run() {
    throw new GoodbyeException("Good bye");
  }
}
