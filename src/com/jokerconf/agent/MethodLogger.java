package com.jokerconf.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

public class MethodLogger implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {

    ClassPool cp = ClassPool.getDefault();
    cp.importPackage("org.asolntsev.agent");

    if (className.startsWith("org/asolntsev/agent") || className.startsWith("java/") || className.startsWith("sun/")) {
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
              "org.asolntsev.agent.Stack.push();" +
              "org.asolntsev.agent.Stack.log(\"" + className + "." + method.getName() + "\"); " +
              "}");
          method.insertAfter("{ org.asolntsev.agent.Stack.pop(); }", true);
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