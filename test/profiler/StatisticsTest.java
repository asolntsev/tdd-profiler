package profiler;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsTest {
  @Test
  public void canPrintStatistics() {
    Statistics statistics = new Statistics();
    statistics.add("com.jokerconf.A.do1", 100L);
    statistics.add("com.jokerconf.B.do2", 200L);
    assertThat(statistics.getResult()).isEqualToIgnoringNewLines(
        "com.jokerconf.A.do1 100 ms\r\n\n" +
            "com.jokerconf.B.do2 200 ms"
    );
  }
}