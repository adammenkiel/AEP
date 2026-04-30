import pl.publicprojects.language.interpreter.Interpreter;

import java.io.*;

public class AlgebraicTest {


    private static ByteArrayOutputStream getAddOutputStream(int a, int b) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);
        data.writeByte(1);
        data.writeByte(0);
        data.writeInt(4 + 1); // length
        data.writeByte(2);
        data.writeInt(a);
        data.writeByte(0);
        data.writeInt(4 + 1); // length
        data.writeByte(2);
        data.writeInt(b);
        return stream;
    }

    private static ByteArrayOutputStream getAddOutputStream(int a, int b, int c) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);
        data.writeByte(1);
        data.writeByte(0); // typeId
        data.writeInt(4 + 1); // length
        data.writeByte(2);
        data.writeInt(a);
        data.writeByte(2); // typeId
        data.writeInt(21); // length
        data.writeByte(1);
        data.writeByte(0);
        data.writeInt(4 + 1); // length
        data.writeByte(2);
        data.writeInt(b);
        data.writeByte(0);
        data.writeInt(4 + 1); // length
        data.writeByte(2);
        data.writeInt(c);
        return stream;
    }
    public static void main(String[] args) throws IOException {

        ByteArrayOutputStream stream = getAddOutputStream(4244, 236, 1233);
        Interpreter i = new Interpreter();
        byte[] bytes = stream.toByteArray();
        System.out.println(
                i.getAlgebraicExpressionManager()
                .getResult(bytes)
                .getValue()
        );

        FileOutputStream fileOutputStream = new FileOutputStream("AEPPublic/programs/addingCode.aep");
        DataOutputStream saveFile = new DataOutputStream(fileOutputStream);
        saveFile.write(bytes);
    }
}
