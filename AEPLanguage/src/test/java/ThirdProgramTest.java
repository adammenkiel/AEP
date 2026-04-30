import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThirdProgramTest {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("AEPPublic/programs/four.aep");
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(3); // command id
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(0); // value


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
        stream.writeInt(0);
        //
        stream.writeInt(21); // len
        stream.writeByte(1); // +
        stream.writeByte(0); // number
        stream.writeInt(5); // len
        stream.writeByte(2); // int
        stream.writeInt(5); // value
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
        stream.writeInt(10000);
        stream.writeByte(0);
        stream.writeInt(4 + 1); // length
        stream.writeByte(2);
        stream.writeInt(0);

        stream.writeInt(0); // if false (len)
    }
}
