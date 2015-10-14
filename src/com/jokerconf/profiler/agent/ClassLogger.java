package com.jokerconf.profiler.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassLogger implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    System.out.println("Transform class " + className);
    return classfileBuffer;
  }
}