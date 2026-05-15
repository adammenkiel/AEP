package pl.publicprojects.aep.tests.interpreter.program.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.publicprojects.aep.tests.interpreter.program.ProgramGenerator;
import pl.publicprojects.aep.tests.interpreter.program.programs.ThirdProgram;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThirdProgramTest {


    public static Stream<Arguments> conditionsData() {
        return Stream.of(
                Arguments.of(0, 5, 10000),
                Arguments.of(2, 5, 10000),
                Arguments.of(5, 5, 10000),
                Arguments.of(100, 5, 10000)
        );
    }

    @ParameterizedTest
    @MethodSource("conditionsData")
    public void thirdProgramTest(int startValue, int ifValue, int addValue) throws IOException {

        ProgramGenerator program = new ThirdProgram(startValue, ifValue, addValue);
        Interpreter interpreter = new Interpreter();
        interpreter.run(
                new LanguageInputStream(
                        interpreter,
                        new ByteArrayInputStream(program.getProgramBytecode())
                )
        );
        assertFalse(interpreter.getCurrentVariables().isEmpty());
        assertNotNull(interpreter.getCurrentVariables().get(0));
        assertInstanceOf(IntegerNumber.class, interpreter.getCurrentVariables().get(0).getValue());

        assertEquals(((IntegerNumber)interpreter.getCurrentVariables().get(0).getValue()).getValue(),
                startValue < ifValue
                        ?
                        (addValue + startValue)
                        :
                        startValue
        );
    }
}
