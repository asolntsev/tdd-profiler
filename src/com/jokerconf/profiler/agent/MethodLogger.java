package com.jokerconf.profiler.agent;

import com.jokerconf.profiler.statistics.Statistics;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

public class MethodLogger implements ClassFileTransformer {
  public MethodLogger(Statistics statistics) {
    MethodInterceptor.statistics = statistics;
  }

  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {

    ClassPool cp = ClassPool.getDefault();
    cp.importPackage("com.jokerconf.profiler.agent");

    if (className.startsWith("java/") || className.startsWith("sun/") ||
        className.startsWith("com/jokerconf/profiler") ||
        className.startsWith("com/intellij")) {
      return classfileBuffer;
    }

    try {
      CtClass ct = cp.makeClass(new ByteArrayInputStream(classfileBuffer));
      if (ct.isInterface()) return classfileBuffer;

      CtMethod[] declaredMethods = ct.getDeclaredMethods();
      for (CtMethod method : declaredMethods) {
        if (Modifier.isAbstract(method.getModifiers())) continue;
        try {
          method.insertBefore(" { " +
              "com.jokerconf.profiler.agent.MethodInterceptor.enter(\"" + className + "\", \"" + method.getName() + "\"); " +
              "}");
          method.insertAfter(" { " +
              "com.jokerconf.profiler.agent.MethodInterceptor.exit(\"" + className + "\", \"" + method.getName() + "\"); " +
              "}", true);
        }
        catch (Throwable e) {
          System.err.println("Transformation failed for " + className + '.' + method.getName() + ": " + e.getMessage());
        }
      }

      return ct.toBytecode();
    } catch (Throwable e) {
      System.err.println("Transformation failed for " + className + ": " + e.getMessage());
      return classfileBuffer;
    }
  }
}