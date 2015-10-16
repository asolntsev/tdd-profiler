package profiler;

import javassist.CtClass;
import org.junit.Test;
import org.mockito.InOrder;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InstrumenterTest {
  private Instrumenter instrumenter = new Instrumenter();
  Class clazz = Hello.class;
  
  @Test
  public void doesNotInstrumentInterfaces() throws Exception {
    byte[] classBytes = Files.readAllBytes(Paths.get(clazz.getResource(clazz.getSimpleName() + ".class").toURI()));
    
    Tracer.instance = mock(Tracer.class);

    CtClass transform = instrumenter.transform(clazz.getName(), classBytes);
    transform.setName("Hello2");
    Runnable hello = (Runnable) transform.toClass().newInstance();
    hello.run();

    verify(Tracer.instance).enter();
    verify(Tracer.instance).exit(Hello.class.getName() + ".run");
  }

}