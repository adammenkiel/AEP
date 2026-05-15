package pl.publicprojects.aep.tests.interpreter.program.programs;

import pl.publicprojects.aep.tests.interpreter.program.ProgramGenerator;

import java.io.*;

/**
 * Program
 * <code>int $0$ = number;</code>
 */
public class FirstProgram extends ProgramGenerator {

    private final int number;

    public FirstProgram(int number) {
        this.number = number;
    }

    @Override
    public byte[] getProgramBytecode() throws IOException {
        ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(3); // command id
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(this.number); // value
        stream.close();
        return fileOutputStream.toByteArray();
    }

}
