package profiler;


import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class Agent {
  public static void premain(String args, Instrumentation instrumentation){
    Runtime.getRuntime().addShutdownHook(new Thread(new PrintProfilerResults()));

    ClassFileTransformer transformer = new Instrumenter();
    instrumentation.addTransformer(transformer);
  }
}