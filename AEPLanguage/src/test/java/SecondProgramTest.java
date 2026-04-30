import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondProgramTest {
    public static void main(String[] args) throws IOException {
        int b = 3, c = 100;
        FileOutputStream fileOutputStream = new FileOutputStream("AEPPublic/programs/three.aep");
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(3); // command id
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(12); // value

        stream.writeInt(7);
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
        stream.writeInt(b);
        stream.writeByte(0);
        stream.writeInt(4 + 1); // length
        stream.writeByte(2);
        stream.writeInt(c);
        // alg end
        stream.close();
    }
}
