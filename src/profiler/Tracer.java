package profiler;

import static java.lang.System.currentTimeMillis;

public class Tracer {
  public static Tracer instance = new Tracer();
  private Statistics statistics = new Statistics();
  
  private long start;

  public void enter() {
    start = currentTimeMillis();
  }

  public void exit(String method) {
    getStatistics().add(method, currentTimeMillis() - start);
  }

  public Statistics getStatistics() {
    return statistics;
  }
}
