package pl.publicprojects.language.interpreter.data.types.conditions;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Class for "while (var) {...}" instruction.
 */
@Getter
public class WhileData extends LanguageData {

    private final Interpreter interpreter;

    public WhileData(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    private byte[] conditionData;
    private byte[] conditionAccepted;

    @Override
    public int getId() {
        return 9;
    }

    /**
     * Reads condition
     * bytes for case when "while" condition is satisfied
     *
     * @param stream We need stream for load settings of this instructions
     * @throws IOException When stream data is wrong
     */
    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.conditionData = stream.readBytesTable();
        this.conditionAccepted = stream.readBytesTable();
    }

    /**
     * Emulates while loop
     * @throws IOException if bytecode is wrong or variables are incorrect
     */
    @Override
    public void execute() throws IOException {

        while(
                this.interpreter.getBooleanExpressionManager()
                    .getResult(this.conditionData)
        )
            this.interpreter.start(
                    new LanguageInputStream(
                            this.interpreter,
                            new ByteArrayInputStream(
                                    this.conditionAccepted
                            )
                    )
            );
    }
}
