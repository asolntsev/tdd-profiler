package profiler;

import demo.HelloWorld;
import demo.HelloWorldWithThrow;
import javassist.CannotCompileException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class InstrumenterTest {
  Instrumenter instrumenter = new Instrumenter();

  @Before
  public void setUp() {
    Tracer.instance = mock(Tracer.class);
  }

  @Test
  public void helloWorldIsNotInstrumentedByDefault() {
    HelloWorld helloWorld = new HelloWorld();
    helloWorld.run();

    verify(Tracer.instance, never()).enter();
    verify(Tracer.instance, never()).exit(null);
  }

  @Test
  public void instrumentsMethods() throws IllegalAccessException, InstantiationException, InterruptedException, IOException, CannotCompileException, URISyntaxException {
    Class instrumentedClass = instrumenter.instrumentClass(HelloWorld.class);
    Runnable helloWorld = (Runnable) instrumentedClass.newInstance();
    helloWorld.run();
    
    verify(Tracer.instance).enter();
    verify(Tracer.instance).exit("demo.HelloWorld.run");
  }

  @Test
  public void instrumentsMethodsAsFinally() throws IllegalAccessException, InstantiationException, InterruptedException, IOException, CannotCompileException, URISyntaxException {
    Class instrumentedClass = instrumenter.instrumentClass(HelloWorldWithThrow.class);
    Runnable helloWorld = (Runnable) instrumentedClass.newInstance();
    try {
      helloWorld.run();
      fail();
    }
    catch (RuntimeException ignore) {
    }
    verify(Tracer.instance).exit("demo.HelloWorldWithThrow.run");
  }

  @Test
  public void shouldNotInstrumentJavaClasses() throws IllegalClassFormatException {
    byte[] bytes = new byte[0];
    assertSame(bytes, instrumenter.transform(null, "java/lang/List", null, null, bytes));
  }
  
  @Test
  public void shouldNotInstrumentSunClasses() throws IllegalClassFormatException {
    byte[] bytes = new byte[0];
    assertSame(bytes, instrumenter.transform(null, "sun/x/y/z", null, null, bytes));
  }

  @Test
  public void shouldNotInstrumentProfilerClasses() throws IllegalClassFormatException {
    byte[] bytes = new byte[0];
    assertSame(bytes, instrumenter.transform(null, "profiler/Agent", null, null, bytes));
  }
}
