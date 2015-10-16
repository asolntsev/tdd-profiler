package profiler;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Instrumenter implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] bytes) throws IllegalClassFormatException {
    if (className.startsWith("java/") || className.startsWith("sun/") ||
        className.startsWith("profiler") ||
        className.startsWith("com/intellij")) {
      return bytes;
    }
    if (classBeingRedefined.isInterface()) return bytes;

    try {
    return transform(className, bytes).toBytecode();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  CtClass transform(String className, byte[] bytes) throws Exception {
      ClassPool cp = ClassPool.getDefault();
      CtClass ct = cp.makeClass(new ByteArrayInputStream(bytes));

      CtMethod[] declaredMethods = ct.getDeclaredMethods();
      for (CtMethod method : declaredMethods) {
        method.insertBefore("profiler.Tracer.instance.enter();");
        method.insertAfter("profiler.Tracer.instance.exit(\"" + className + "." + method.getName() + "\");", true);
      }
      return ct;
  }
}