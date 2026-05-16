package pl.publicprojects.language.interpreter.data.types;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

/**
 * Instruction for set Variable with fixed nameId to result of bytes[] expression
 */
@Getter
public class ModifyVariableData extends LanguageData {

    private int nameId;
    private byte[] bytes;
    private final Interpreter interpreter;

    public ModifyVariableData(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * Instruction ID
     * @return 7
     */
    @Override
    public int getId() {
        return 7;
    }

    /**
     * Function for read data from bytecode.
     *
     * @param stream We need stream for load settings of this instructions
     * @throws IOException When byteCode is wrong
     */
    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.nameId = stream.readInt();
        this.bytes = stream.readAllBytes();
    }

    /**
     * Executes instruction with fixed settings.
     * @throws IOException When byte array expression is wrong or there is no variable with id nameId
     */
    @Override
    public void execute() throws IOException {
        var value = this.interpreter.getAlgebraicExpressionManager()
                .getResult(this.bytes);

        this.interpreter.getCurrentVariableByNameId(this.nameId)
                .setValue(value);
    }
}
