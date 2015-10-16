package profiler;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

public class Instrumenter implements ClassFileTransformer {
  Class instrumentClass(Class clazz) throws IOException, CannotCompileException, URISyntaxException {
    byte[] bytes = Files.readAllBytes(Paths.get(clazz.getResource(clazz.getSimpleName() + ".class").toURI()));
    CtClass ctClass = instrument(bytes);
    ctClass.setName(clazz.getName() + "$joker");
    return ctClass.toClass();
  }

  private CtClass instrument(byte[] classBytes) throws IOException, CannotCompileException {
    ClassPool classPool = ClassPool.getDefault();
    CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classBytes));

    for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
      ctMethod.insertBefore("profiler.Tracer.instance.enter();");
      ctMethod.insertAfter("profiler.Tracer.instance.exit(\"" + ctClass.getName() + "." + ctMethod.getName() + "\");", true);
    }
    return ctClass;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
    if (className.startsWith("java/") || className.startsWith("sun/") || className.startsWith("profiler/")) return classBytes;
    
    try {
      CtClass instrumentedClass = instrument(classBytes);
      return instrumentedClass.toBytecode();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
