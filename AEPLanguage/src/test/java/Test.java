import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        //ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("AEPPublic/programs/second.aep");
        DataOutputStream stream = new DataOutputStream(fileOutputStream);
        stream.writeInt(0); // command id
        stream.writeInt(8); // len
        stream.writeInt(0); // nameId
        stream.writeInt(12); // value
        stream.close();

    }
}
