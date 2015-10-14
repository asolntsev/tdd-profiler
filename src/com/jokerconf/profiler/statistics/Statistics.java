package com.jokerconf.profiler.statistics;

import java.util.*;

public class Statistics {
  public void methodCompletedIn(String clazz, String method, long executionTimeNs) {
    System.out.println("Executed " + clazz + "." + method + " in " + executionTimeNs / 1000000 + " ms.");
  }
}
