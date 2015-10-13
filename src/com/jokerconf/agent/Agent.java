package com.jokerconf.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class Agent {
  public static void premain(String args, Instrumentation instrumentation){
//    ClassFileTransformer transformer = new ClassLogger();
    ClassFileTransformer transformer = new MethodLogger();
    instrumentation.addTransformer(transformer);
  }
}