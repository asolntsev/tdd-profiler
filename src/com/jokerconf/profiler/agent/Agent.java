package com.jokerconf.profiler.agent;

import com.jokerconf.profiler.statistics.PrintProfilerResults;
import com.jokerconf.profiler.statistics.Statistics;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class Agent {
  public static void premain(String args, Instrumentation instrumentation){
    Statistics statistics = new Statistics();

    Runtime.getRuntime().addShutdownHook(new Thread(new PrintProfilerResults(statistics)));

    ClassFileTransformer transformer = new MethodLogger(statistics);
    instrumentation.addTransformer(transformer);
  }
}