package pl.publicprojects.aep.tests.interpreter.expression.tests;

import org.junit.jupiter.api.Test;
import pl.publicprojects.language.interpreter.Interpreter;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlgebraicTest {

    // Expression: a + b
    private ByteArrayOutputStream getAddOutputStream(int a, int b) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);
        data.writeByte(1); // +

        data.writeByte(0); // LanguageNumber
        data.writeInt(4 + 1); // length
        data.writeByte(2); // type of number, 2 = int (AlgebraicExpressionManager#getLangNumberByID)
        data.writeInt(a); // value

        data.writeByte(0); // LanguageNumber
        data.writeInt(4 + 1); // length
        data.writeByte(2); // type of number, 2 = int (AlgebraicExpressionManager#getLangNumberByID)
        data.writeInt(b); // value
        return stream;
    }

    // Expression:  a + b + c
    private ByteArrayOutputStream getAddOutputStream(int a, int b, int c) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);
        data.writeByte(1); // +

        data.writeByte(0); // LanguageNumber
        data.writeInt(4 + 1); // length
        data.writeByte(2); // type of number, 2 = int (AlgebraicExpressionManager#getLangNumberByID)
        data.writeInt(a); // value

        data.writeByte(2); // Expression
        data.writeInt(21); // length
        data.writeByte(1); // +
        data.writeByte(0); // LanguageNumber
        data.writeInt(4 + 1); // length
        data.writeByte(2); // type of number, 2 = int (AlgebraicExpressionManager#getLangNumberByID)
        data.writeInt(b); // value
        data.writeByte(0); // LanguageNumber
        data.writeInt(4 + 1); // length
        data.writeByte(2); // type of number, 2 = int (AlgebraicExpressionManager#getLangNumberByID)
        data.writeInt(c); // value
        return stream;
    }


    @Test
    public void algebraABInterpreterTest() throws IOException {
        int a = 4244;
        int b = 236;

        ByteArrayOutputStream stream = getAddOutputStream(a, b);
        Interpreter i = new Interpreter();
        byte[] bytes = stream.toByteArray();
        assertNotNull(bytes);

        Object res = i.getAlgebraicExpressionManager()
                .getResult(bytes)
                .getValue();
        assertInstanceOf(Integer.class, res);
        int numberFormRes = (int) res;
        assertEquals(numberFormRes, a + b);
    }

    @Test
    public void algebraABCInterpreterTest() throws IOException {
        int a = 4244;
        int b = 236;
        int c = 1233;

        ByteArrayOutputStream stream = getAddOutputStream(a, b, c);
        Interpreter i = new Interpreter();
        byte[] bytes = stream.toByteArray();
        assertNotNull(bytes);

        Object res = i.getAlgebraicExpressionManager()
            .getResult(bytes)
            .getValue();

        assertInstanceOf(Integer.class, res);
        int numberFormRes = (int) res;
        assertEquals(numberFormRes, a + b + c);
    }
}
