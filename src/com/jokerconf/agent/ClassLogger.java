package com.jokerconf.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

public class ClassLogger implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    System.out.println("Transform class " + className);
//    try {
//      Path path = Paths.get("classes/" + className + ".class");
//      Files.write(path, bytes);
//    } 
//    catch (Throwable ignored) { 
//    }

    return classfileBuffer;
  }
}