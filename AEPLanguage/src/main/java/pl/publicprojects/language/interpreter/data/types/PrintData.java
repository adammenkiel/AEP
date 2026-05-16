package pl.publicprojects.language.interpreter.data.types;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

/**
 * Print variable by System.out.println(...)
 */
@Getter
public class PrintData extends LanguageData {

    private int nameId;
    private final Interpreter interpreter;

    public PrintData(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 10;
    }

    /**
     * Reads variable id that it will print
     * @param stream We need stream for load settings of this instructions
     * @throws IOException When bytecode is wrong
     */
    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.nameId = stream.readInt();
    }

    /**
     * Prints variable
     */
    @Override
    public void execute() throws IOException {
        System.out.println(
                this.interpreter
                        .getCurrentVariableByNameId(this.nameId)
                        .getValue()
        );
    }
}
