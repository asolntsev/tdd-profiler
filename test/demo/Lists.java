package demo;

import java.util.ArrayList;
import java.util.LinkedList;

public class Lists {
  public static void main(String[] args) {
    System.out.println("Size: " + fillLongArrayList());
    System.out.println("Size: " + fillLongLinkedList());
  }

  private static int fillLongArrayList() {
    ArrayList<Object> list = new ArrayList<>();
    for (int i = 0; i < 10000000; i++) {
      list.add(i);
    }
    return list.size();
  }

  private static int fillLongLinkedList() {
    LinkedList<Object> list = new LinkedList<>();
    for (int i = 0; i < 10000000; i++) {
      list.add(i);
    }
    return list.size();
  }
}
