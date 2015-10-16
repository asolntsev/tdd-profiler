package profiler;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TracerTest {
  @Before
  public void setUp() {
    Tracer.instance = new Tracer();
  }

  @Test
  public void initialStatistics() {
    assertEquals(0, Tracer.instance.getStatistics().getResult().length());
  }

  @Test
  public void countsMethodsExecutions() {
    Tracer.instance.enter();
    Tracer.instance.exit(null);
    
    assertEquals(1, Tracer.instance.getStatistics().getExecutionTime().size());
  }
  
  @Test
  public void tracesMethodsExecutionsTime() throws InterruptedException {
    Tracer.instance.enter();
    Thread.sleep(5);
    Tracer.instance.exit(null);

    long time = Tracer.instance.getStatistics().getExecutionTime().get(0);
    assertTrue(time + "ms", time >= 5);
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
    assertEquals(2, time.size());
    assertTrue(time + "ms", time.get(0) >= 5);
    assertTrue(time + "ms", time.get(1) >= 6);
  }
}