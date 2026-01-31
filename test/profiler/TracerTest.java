package profiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TracerTest {
  @BeforeEach
  public void setUp() {
    Tracer.instance = new Tracer();
  }

  @Test
  public void initialStatistics() {
    assertThat(Tracer.instance.getStatistics().getResult()).hasSize(0);
  }

  @Test
  public void countsMethodsExecutions() {
    Tracer.instance.enter();
    Tracer.instance.exit(null);
    
    assertThat(Tracer.instance.getStatistics().getExecutionTime()).hasSize(1);
  }
  
  @Test
  public void tracesMethodsExecutionsTime() throws InterruptedException {
    Tracer.instance.enter();
    Thread.sleep(5);
    Tracer.instance.exit(null);

    long time = Tracer.instance.getStatistics().getExecutionTime().get(0);
    assertThat(time).isGreaterThanOrEqualTo(5);
  }

  @Test
  public void tracesMultipleMethodExecutionTimes() throws InterruptedException {
    Tracer.instance.enter();
    Thread.sleep(5);
    Tracer.instance.exit("do");

    Tracer.instance.enter();
    Thread.sleep(6);
    Tracer.instance.exit("do");

    List<Long> time = Tracer.instance.getStatistics().getExecutionTime();
    assertThat(time).hasSize(2);
    assertThat(time.get(0)).isGreaterThanOrEqualTo(5);
    assertThat(time.get(1)).isGreaterThanOrEqualTo(6);
  }
}