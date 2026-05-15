package pl.publicprojects.aep.tests.interpreter.program.tests;

import org.junit.jupiter.api.Test;
import pl.publicprojects.aep.tests.interpreter.program.ProgramGenerator;
import pl.publicprojects.aep.tests.interpreter.program.programs.ForthProgram;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class ForthProgramTest {

    @Test
    public void fourProgramTest() throws IOException {
        int stopNumber = 20;
        ProgramGenerator program = new ForthProgram(0, stopNumber);

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
        assertEquals(((IntegerNumber)interpreter.getCurrentVariables().get(0).getValue()).getValue(), stopNumber);
    }

}
