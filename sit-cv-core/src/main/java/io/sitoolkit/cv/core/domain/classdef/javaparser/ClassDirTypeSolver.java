package io.sitoolkit.cv.core.domain.classdef.javaparser;

import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.model.SymbolReference;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassDirTypeSolver implements TypeSolver {

  private static ClassDirTypeSolver instance;

  @Getter @Setter private TypeSolver parent;
  /** key:class name, value:path to class file */
  private Map<String, Path> classFileMap = new HashMap<>();

  private ClassPool classPool = new ClassPool(false);

  private static final String CLASS_SUFFIX = ".class";

  public static synchronized ClassDirTypeSolver get(Path classDir) {
    if (instance == null) {
      instance = new ClassDirTypeSolver();
    }
    instance.addClassDir(classDir);
    return instance;
  }

  private void addClassDir(Path classDir) {

    try (Stream<Path> paths = Files.walk(classDir)) {
      classPool.appendClassPath(classDir.toString());
      classPool.appendSystemPath();

      paths
          .filter(path -> path.getFileName().toString().endsWith(CLASS_SUFFIX))
          .forEach(
              path -> {
                classFileMap.put(classFilePathToClassName(classDir, path), path);
              });
    } catch (IOException | NotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private String classFilePathToClassName(Path classDir, Path classFilePath) {
    String classFilePathStr = classDir.relativize(classFilePath).toString();
    if (!classFilePathStr.endsWith(CLASS_SUFFIX)) {
      throw new IllegalStateException();
    }
    String className =
        classFilePathStr.substring(0, classFilePathStr.length() - CLASS_SUFFIX.length());
    className = className.replace('/', '.');
    className = className.replace('$', '.');
    return className;
  }

  @Override
  public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
    try {
      Path classFilePath = classFileMap.get(name);
      if (classFilePath != null) {

        try (InputStream is = Files.newInputStream(classFilePath)) {
          CtClass ctClass = classPool.makeClass(is);
          return SymbolReference.solved(JavassistFactory.toTypeDeclaration(ctClass, getRoot()));
        }

      } else {
        return SymbolReference.unsolved();
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
