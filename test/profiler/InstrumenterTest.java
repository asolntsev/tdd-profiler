package profiler;

import demo.GoodbyeException;
import demo.HelloWorld;
import demo.HelloWorldWithThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class InstrumenterTest {
  Instrumenter instrumenter = new Instrumenter();

  @BeforeEach
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
  public void instrumentsMethods() throws Exception {
    Class<?> instrumentedClass = instrumenter.instrumentClass(HelloWorld.class);
    Runnable helloWorld = (Runnable) instrumentedClass.getDeclaredConstructor().newInstance();

    helloWorld.run();
    
    verify(Tracer.instance).enter();
    verify(Tracer.instance).exit("demo.HelloWorld.run");
  }

  @Test
  public void instrumentsMethodsAsFinally() throws Exception {
    Class<?> instrumentedClass = instrumenter.instrumentClass(HelloWorldWithThrow.class);
    Runnable helloWorld = (Runnable) instrumentedClass.getDeclaredConstructor().newInstance();
    assertThatThrownBy(helloWorld::run)
        .isInstanceOf(GoodbyeException.class)
        .hasMessage("Good bye");
    verify(Tracer.instance).exit("demo.HelloWorldWithThrow.run");
  }

  @Test
  public void shouldNotInstrumentJavaClasses() throws IllegalClassFormatException {
    byte[] bytes = new byte[0];
    assertThat(instrumenter.transform(null, "java/lang/List", null, null, bytes)).isSameAs(bytes);
  }
  
  @Test
  public void shouldNotInstrumentSunClasses() throws IllegalClassFormatException {
    byte[] bytes = new byte[0];
    assertThat(instrumenter.transform(null, "sun/x/y/z", null, null, bytes)).isSameAs(bytes);
  }

  @Test
  public void shouldNotInstrumentProfilerClasses() throws IllegalClassFormatException {
    byte[] bytes = new byte[0];
    assertThat(instrumenter.transform(null, "profiler/Agent", null, null, bytes)).isSameAs(bytes);
  }
}
