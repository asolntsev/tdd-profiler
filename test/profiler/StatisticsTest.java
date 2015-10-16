package profiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsTest {
  @Test
  public void canPrintStatistics() {
    Statistics statistics = new Statistics();
    statistics.add("com.jokerconf.A.do1", 100L);
    statistics.add("com.jokerconf.B.do2", 200L);
    assertEquals("com.jokerconf.A.do1 100 ms\n" +
        "com.jokerconf.B.do2 200 ms", statistics.getResult());
  }
}