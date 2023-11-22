package io.sitoolkit.cv.core.domain.classdef.javaparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.javaparser.ast.CompilationUnit;
import io.sitoolkit.cv.core.domain.classdef.CvStatement;
import io.sitoolkit.cv.core.domain.classdef.CvStatementDefaultImpl;
import io.sitoolkit.cv.core.domain.classdef.LoopStatement;
import io.sitoolkit.cv.core.domain.classdef.MethodCallDef;
import io.sitoolkit.cv.core.domain.classdef.MethodDef;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatementVisitorLoopTest extends StatementVisitorTestBase {

  static CompilationUnit compilationUnit;

  @BeforeAll
  static void init() throws IOException {
    compilationUnit = parseFile("src/main/java/a/b/c/LoopController.java");
  }

  @Test
  void simpleFor() throws IOException {
    testFlatLoop(getTestMethodName());
  }

  @Test
  void forEach() throws IOException {
    testFlatLoop(getTestMethodName());
  }

  @Test
  void streamMethodRef() throws IOException {
    testFlatLoop(getTestMethodName());
  }

  @Test
  void streamLambda() throws IOException {
    testFlatLoop(getTestMethodName());
  }

  private void testFlatLoop(String method) throws IOException {
    MethodDef methodDef = getVisitResult(compilationUnit, "LoopController", method);

    List<CvStatement> loopStatements =
        methodDef.getStatements().stream()
            .filter(LoopStatement.class::isInstance)
            .collect(Collectors.toList());

    assertThat(loopStatements.size(), is(1));

    List<CvStatement> statementsInLoop =
        ((CvStatementDefaultImpl) loopStatements.get(0)).getChildren();

    assertThat(statementsInLoop.size(), is(1));

    MethodCallDef methodCall = (MethodCallDef) statementsInLoop.get(0);
    assertThat(methodCall.getName(), is("process"));
  }
}
