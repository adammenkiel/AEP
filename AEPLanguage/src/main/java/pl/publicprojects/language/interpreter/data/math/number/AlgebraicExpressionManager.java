package pl.publicprojects.language.interpreter.data.math.number;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.*;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/*
* Maybe stack method of expressions notation would be better
* but at the start I wanted to create possibility for easy decompile.
 * */

@Getter
public class AlgebraicExpressionManager {

    private final Interpreter interpreter;

    public AlgebraicExpressionManager(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    private LanguageNumber<?> count(int id, LanguageNumber<?> a, LanguageNumber<?> b) {
        return switch (id) {
            case 0 -> a.minus(b);
            case 1 -> a.plus(b);
            case 2 -> a.divide(b);
            case 3 -> a.multiple(b);
            case 4 -> a.power(b);
            default -> null;
        };
    }

    public LanguageNumber<?> getLangNumberByID(int id) {
        return switch (id) {
            case 0 -> new ByteNumber();
            case 1 -> new ShortNumber();
            case 2 -> new IntegerNumber();
            case 3 -> new LongNumber();
            case 4 -> new FloatNumber();
            case 5 -> new DoubleNumber();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public LanguageNumber<?> getResult(byte[] expression) throws IOException {
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(expression);
        LanguageInputStream languageInputStream = new LanguageInputStream(bytesStream);
        return this.parse(languageInputStream);
    }

    private LanguageNumber<?> readNumber(LanguageInputStream languageInputStream) throws IOException {
        int typeId = languageInputStream.readByte(); // number(0) or variable(1) or algebraicExpression
        LanguageNumber<?> numberFirst = null;
        if(typeId == 0) {
            byte[] bytes = languageInputStream.readBytesTable();
            ByteArrayInputStream numberStream = new ByteArrayInputStream(bytes);
            LanguageInputStream lNumberStream = new LanguageInputStream(numberStream);
            byte numberType = lNumberStream.readByte(); // type of number
            numberFirst = this.getLangNumberByID(numberType);
            numberFirst.read(lNumberStream);
        } else if(typeId == 1) {
            int nameId = languageInputStream.readInt(); // id zmiennej
            VariableData currentVariable = this.interpreter.getCurrentVariableByNameId(nameId);
            if(currentVariable == null) {
                System.out.println("Null!!! " + nameId);
            }
            numberFirst = (LanguageNumber<?>)currentVariable.getValue();
        } else if(typeId == 2) {
            int len = languageInputStream.readInt();
            byte[] bytes = new byte[len];
            languageInputStream.readFully(bytes);
            ByteArrayInputStream expressionStream = new ByteArrayInputStream(bytes);
            LanguageInputStream lExpressionStream = new LanguageInputStream(expressionStream);
            numberFirst = this.parse(lExpressionStream);
        }
        return numberFirst;
    }

    private LanguageNumber<?> parse(LanguageInputStream languageInputStream) throws IOException {
        int operationId = languageInputStream.readByte(); // plus minus etc
        if(operationId == 127) {
            return this.readNumber(languageInputStream);
        }
        LanguageNumber<?> numberFirst = this.readNumber(languageInputStream);
        LanguageNumber<?> numberSecond = this.readNumber(languageInputStream);
        return this.count(operationId, numberFirst, numberSecond);
    }


}
