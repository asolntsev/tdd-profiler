package com.jokerconf.profiler.agent;

import com.jokerconf.profiler.statistics.Statistics;

import java.util.Stack;

public class MethodInterceptor {
  static Statistics statistics;
  static ThreadLocal<Stack<Long>> currentMethodExecutionTime = new ThreadLocal<Stack<Long>>() {
    @Override
    protected Stack<Long> initialValue() {
      return new Stack<>();
    }
  };
  
  public static void enter(String clazz, String method) {
    currentMethodExecutionTime.get().push(System.nanoTime());
  }

  public static void exit(String clazz, String method) {
    long end = System.nanoTime();
    long start = currentMethodExecutionTime.get().pop();
    statistics.methodCompletedIn(clazz, method, end-start);
  }
}
