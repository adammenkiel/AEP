package pl.publicprojects.language.interpreter.data.math.number;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.*;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

 /**
  * Class for calculate expressions from byte[] form.
  */

@Getter
public class AlgebraicExpressionManager {

    private final Interpreter interpreter;

    /**
     * Constructor of class for calculating expression
     * @param interpreter Interpreter is used for load variable values
     */
    public AlgebraicExpressionManager(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

     /**
      * Function for get result of atom algebraic expressions
      *
      * @param id operation (+, -, /, *, ^)
      * @param a First number
      * @param b Second number
      * @return Result value in LanguageNumber<?> form
      */
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

    /**
     * Method for parse id to LanguageNumber<?> form
     *
     * @param id ID of LanguageNumber<?>
     *           0 - byte
     *           1 - short
     *           2 - integer
     *           3 - long
     *           4 - float
     *           5 - double
     *           6 - double vector
     * @return Returns LanguageNumber with proper type
     */
    public LanguageNumber<?> getLangNumberByID(int id) {
        return switch (id) {
            case 0 -> new ByteNumber();
            case 1 -> new ShortNumber();
            case 2 -> new IntegerNumber();
            case 3 -> new LongNumber();
            case 4 -> new FloatNumber();
            case 5 -> new DoubleNumber();
            case 6 -> new DoubleVectorNumber();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

     /**
      * Suggested function for get result of algebraic expression in byte[] form
      *
      * @param expression Expression in byte[] form (bytecode of expression)
      * @return Result in LanguageNumber form
      * @throws IOException Throws when byte[] is broken, refers to variable that doesn't exist or getValue of variable is incorrect
      */
    public LanguageNumber<?> getResult(byte[] expression) throws IOException {
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(expression);
        LanguageInputStream languageInputStream = new LanguageInputStream(this.interpreter, bytesStream);
        return this.parse(languageInputStream);
    }

     /**
      * Method for read number from byte code, we have three types of numbers:
      *     - Number (LanguageNumber)
      *     - Variable (Variable data)
      *     - Algebraic (for example 2 + 3 or 2 + a, if we get algebraic type we need to calculate it first)
      *
      * @param languageInputStream Usually byte[] stream created in getResult(byte[]) method
      * @return Number in language number form
      * @throws IOException Throws when byte[] is broken, refers to variable that doesn't exist or getValue of variable is incorrect
      */
    private LanguageNumber<?> readNumber(LanguageInputStream languageInputStream) throws IOException {
        int typeId = languageInputStream.readByte(); // number(0) or variable(1) or algebraicExpression
        LanguageNumber<?> numberFirst = null;
        if(typeId == 0) {
            byte[] bytes = languageInputStream.readBytesTable();
            ByteArrayInputStream numberStream = new ByteArrayInputStream(bytes);
            LanguageInputStream lNumberStream = new LanguageInputStream(this.interpreter, numberStream);
            byte numberType = lNumberStream.readByte(); // type of number
            numberFirst = this.getLangNumberByID(numberType);
            numberFirst.read(lNumberStream);
        } else if(typeId == 1) {
            int nameId = languageInputStream.readInt(); // variable id
            VariableData currentVariable = this.interpreter.getCurrentVariableByNameId(nameId);
            if(currentVariable == null) {
                System.out.println("Variable is null, nameId: " + nameId);
            }
            numberFirst = (LanguageNumber<?>)currentVariable.getValue();
        } else if(typeId == 2) {
            int len = languageInputStream.readInt();
            byte[] bytes = new byte[len];
            languageInputStream.readFully(bytes);
            ByteArrayInputStream expressionStream = new ByteArrayInputStream(bytes);
            LanguageInputStream lExpressionStream = new LanguageInputStream(this.interpreter, expressionStream);
            numberFirst = this.parse(lExpressionStream);
        }
        return numberFirst;
    }

     /**
      * Method for expression parse, we accept nodes with one or two vertex.
      * Just one only when we have situation when we need to get a single number (getNumber)
      * Two vertices when we need to calculate expression.
      *
      * @param languageInputStream Stream usually from getValue(byte[])
      * @return Number in LanguageNumber<?> form.
      * @throws IOException Throws when byte[] is broken, refers to variable that doesn't exist or getValue of variable is incorrect
      */
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
