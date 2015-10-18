package profiler;

public class PrintProfilerResults implements Runnable {
  @Override
  public void run() {
    System.out.println(Tracer.instance.getStatistics().getResult());
  }
}
