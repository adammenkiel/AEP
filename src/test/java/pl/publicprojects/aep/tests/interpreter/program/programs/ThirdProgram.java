package pl.publicprojects.aep.tests.interpreter.program.programs;

import pl.publicprojects.aep.tests.interpreter.program.ProgramGenerator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//"AEPPublic/programs/four.aep"
/**
 * Simplified form of that code:
 * <code><br/>
 * int $0$ = startValue; <br/>
 * if($0$ < ifValue) {<br/>
 *     $0$ = $0$ + setValue;<br/>
 * }<br/>
 * </code>
 * We omit writing "+ 0" in pseudocode above.
 */
public class ThirdProgram extends ProgramGenerator {


    private final int startValue;
    private final int ifValue;
    private final int addValue;

    public ThirdProgram(int startValue, int ifValue, int addValue) {
        this.startValue = startValue;
        this.ifValue = ifValue;
        this.addValue = addValue;
    }

    @Override
    public byte[] getProgramBytecode() throws IOException {
        ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(3); // command id
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(this.startValue); // value


        stream.writeInt(8); // command id conditionData
        stream.writeInt(48 + 52 + 4); // len
        stream.writeInt(48); // condition len
        stream.writeByte(0);
        stream.writeByte(3);
        stream.writeByte(0); // <
        //
        stream.writeInt(16); // len
        stream.writeByte(1); // +
        stream.writeByte(1); // variable
        stream.writeInt(0); // variable id
        stream.writeByte(0); // number
        stream.writeInt(5); // len
        stream.writeByte(2); // int
        stream.writeInt(0); // value
        //
        stream.writeInt(21); // len
        stream.writeByte(1); // +
        stream.writeByte(0); // number
        stream.writeInt(4 + 1); // len
        stream.writeByte(2); // int
        stream.writeInt(this.ifValue); // value
        stream.writeByte(0); // number
        stream.writeInt(5); // len
        stream.writeByte(2); // int
        stream.writeInt(0); // value

        stream.writeInt(44); // if true (len)
        stream.writeInt(7); // opcode modifyvalue
        stream.writeInt(36); // len
        stream.writeInt(0); // nameId
        // alg start
        stream.writeByte(1);
        stream.writeByte(1); // typeId
        stream.writeInt(0);
        stream.writeByte(2); // typeId
        stream.writeInt(21); // length
        stream.writeByte(1);
        stream.writeByte(0);
        stream.writeInt(4 + 1); // length
        stream.writeByte(2);
        stream.writeInt(this.addValue);
        stream.writeByte(0);
        stream.writeInt(4 + 1); // length
        stream.writeByte(2);
        stream.writeInt(0);

        stream.writeInt(0); // if false (len)
        return fileOutputStream.toByteArray();
    }
}
