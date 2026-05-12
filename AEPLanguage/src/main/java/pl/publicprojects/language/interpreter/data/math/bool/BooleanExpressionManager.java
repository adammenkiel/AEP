package pl.publicprojects.language.interpreter.data.math.bool;

import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Getter
public class BooleanExpressionManager {

    private final Interpreter interpreter;

    public BooleanExpressionManager(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    private boolean count(int id, boolean a, boolean b) {
        return switch (id) {
            case 1 -> a && b;
            case 2 -> a || b;
            case 3 -> a == b;
            case 4 -> !a;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private boolean countRelation(int id, LanguageNumber<?> a, LanguageNumber<?> b) {
        if(a.getValue() instanceof INDArray || b.getValue() instanceof INDArray) {
            throw new RuntimeException("Unsupported type");
        }

        return switch (id) {
            case 0 -> a.less(b); // a < b
            case 1 -> b.less(a); // b > a
            case 2 -> !b.less(a); // a <= b
            case 3 -> !a.less(b); // a => b
            case 4 -> !b.less(a) && !a.less(b); // a == b
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private boolean readBooleanE(LanguageInputStream languageInputStream) throws IOException {
        byte typeId = languageInputStream.readByte();
        if(typeId == 0) {
            //value true/false
            return languageInputStream.readBoolean();
        }
        if(typeId == 1) {
            //boolean variable
            int nameId = languageInputStream.readInt(); // id zmiennej
            VariableData currentVariable = this.interpreter.getCurrentVariableByNameId(nameId);
            return (Boolean)currentVariable.getValue();
        }
        if(typeId == 2) {
            // another expression
            byte[] bytes = languageInputStream.readBytesTable();
            ByteArrayInputStream expressionStream = new ByteArrayInputStream(bytes);
            LanguageInputStream lExpressionStream = new LanguageInputStream(expressionStream);
            return this.parse(lExpressionStream);
        }
        if(typeId == 3) {
            //inequality
            byte relationId = languageInputStream.readByte();
            byte[] firstExpressionBytes = languageInputStream.readBytesTable();
            byte[] secondExpressionBytes = languageInputStream.readBytesTable();

            LanguageNumber<?> firstNumber = this.interpreter
                    .getAlgebraicExpressionManager()
                    .getResult(firstExpressionBytes);
            LanguageNumber<?> secondNumber = this.interpreter
                    .getAlgebraicExpressionManager()
                    .getResult(secondExpressionBytes);
            return this.countRelation(relationId, firstNumber, secondNumber);
        }
        return false;
    }

    private boolean parse(LanguageInputStream languageInputStream) throws IOException {
        int operationId = languageInputStream.readByte();
        if(operationId == 0) {
            return this.readBooleanE(languageInputStream);
        }
        if(operationId <= 3) {
            boolean one = this.readBooleanE(languageInputStream);
            boolean two = this.readBooleanE(languageInputStream);
            return this.count(operationId, one, two);
        }
        if(operationId == 4) {
            boolean one = this.readBooleanE(languageInputStream);
            return this.count(operationId, one, one);
        }
        return false;
    }
    public boolean getResult(byte[] expression) throws IOException {
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(expression);
        LanguageInputStream languageInputStream = new LanguageInputStream(bytesStream);
        return this.parse(languageInputStream);
    }
}
