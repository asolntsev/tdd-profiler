package demo;

import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;

public class Selenium {
  public static void main(String[] args) {
    for (int i = 0; i < 5; i++) {
      open("http://google.com");
      close();
    }
  }
}
