package pl.publicprojects.aep.tests.interpreter.program.programs;

import pl.publicprojects.aep.tests.interpreter.program.ProgramGenerator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//"AEPPublic/programs/three.aep"
/**
 * <code>
 * $0$ = 12;<br />
 * $0$ = $0$ + firstNumber + secondNumber;<br />
 * </code>
 */
public class SecondProgram extends ProgramGenerator {

    private final int startVal;
    private final int firstNumber;
    private final int secondNumber;

    public SecondProgram(int startVal, int firstNumber, int secondNumber) {
        this.startVal = startVal;
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    @Override
    public byte[] getProgramBytecode() throws IOException {
        ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(3); // command id (create variable)
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(this.startVal); // value

        stream.writeInt(7); // command id (modify variable)
        stream.writeInt(36); // length of instruction
        stream.writeInt(0); // Id of variable we want to modify
        // Expression
        stream.writeByte(1); // +
        stream.writeByte(1); // VariableData
        stream.writeInt(0); // nameId (name of that variable)
        stream.writeByte(2); // Continue expression
        stream.writeInt(21); // length
        stream.writeByte(1); // +
        stream.writeByte(0); // LanguageNumber
        stream.writeInt(4 + 1); // length
        stream.writeByte(2); // Integer
        stream.writeInt(this.firstNumber); // value
        stream.writeByte(0); // LanguageNumber
        stream.writeInt(4 + 1); // length
        stream.writeByte(2); // Integer
        stream.writeInt(this.secondNumber); // value
        // alg end
        stream.close();
        return fileOutputStream.toByteArray();
    }
}
