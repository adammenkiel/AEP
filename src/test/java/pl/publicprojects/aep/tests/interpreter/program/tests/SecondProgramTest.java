package pl.publicprojects.aep.tests.interpreter.program.tests;

import org.junit.jupiter.api.Test;
import pl.publicprojects.aep.tests.interpreter.program.ProgramGenerator;
import pl.publicprojects.aep.tests.interpreter.program.programs.SecondProgram;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecondProgramTest {

    @Test
    public void secondProgramTest() throws IOException {
        int startVal = 12;
        int fNumber = 100;
        int sNumber = 3;

        ProgramGenerator program = new SecondProgram(startVal, fNumber, sNumber);

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
        assertEquals(((IntegerNumber)interpreter.getCurrentVariables().get(0).getValue()).getValue(), startVal + fNumber + sNumber);
    }
}
