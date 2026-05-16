package pl.publicprojects.language.interpreter.data.types.conditions;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

/**
 * Class for "if (var) {...} else {...}" instruction.
 */
@Getter
public class ConditionData extends LanguageData {

    private byte[] conditionData;
    private LanguageInputStream conditionAccepted;
    private LanguageInputStream conditionFailed;
    private final Interpreter interpreter;


    public ConditionData(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * ID of instruction
     * @return 8
     */
    @Override
    public int getId() {
        return 8;
    }

    /**
     * Reads condition,
     * bytes for case when "if" condition is satisfied,
     * bytes for case when "if" condition is failed
     *
     * @param stream We need stream for load settings of this instructions
     * @throws IOException When stream data is wrong
     */
    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.conditionData = stream.readBytesTable();
        this.conditionAccepted = stream.readLInputStream();
        this.conditionFailed = stream.readLInputStream();
    }

    /**
     * Emulates condition
     * @throws IOException if bytecode is wrong or variables are incorrect
     */
    @Override
    public void execute() throws IOException {
        final Interpreter interpreter = this.interpreter;

        boolean condition = interpreter.getBooleanExpressionManager()
                .getResult(this.conditionData);

        if(condition) interpreter.start(this.conditionAccepted);
        else interpreter.start(this.conditionFailed);
    }
}
