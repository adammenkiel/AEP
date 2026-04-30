import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FourProgramTest {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("AEPPublic/programs/five.aep");
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(3); // command id
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(0); // value


        stream.writeInt(9); // command id conditionData
        stream.writeInt(44 + 32); // len
        stream.writeInt(28); // condition len
        stream.writeByte(0);
        stream.writeByte(3);
        stream.writeByte(0); // <
        //
        stream.writeInt(6); // len
        stream.writeByte(127); // idx.
        stream.writeByte(1); // variable
        stream.writeInt(0); // variable id
        //
        stream.writeInt(11); // len
        stream.writeByte(127); // idx.
        stream.writeByte(0); // number
        stream.writeInt(5); // len
        stream.writeByte(2); // int
        stream.writeInt(20); // value

        stream.writeInt(40); // if true (len)
        // {

        stream.writeInt(10); //opcode
        stream.writeInt(4); // len
        stream.writeInt(0);

        stream.writeInt(7); // opcode modifyvalue
        stream.writeInt(20); // len
        stream.writeInt(0); // nameId
        // alg start
        stream.writeByte(1); // +
        stream.writeByte(1); // typeId
        stream.writeInt(0);
        stream.writeByte(0); // typeId
        stream.writeInt(4 + 1); // length
        stream.writeByte(2);
        stream.writeInt(1);

        // }
    }
}
