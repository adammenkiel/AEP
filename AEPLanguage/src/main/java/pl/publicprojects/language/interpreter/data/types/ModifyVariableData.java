package pl.publicprojects.language.interpreter.data.types;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

@Getter
public class ModifyVariableData extends LanguageData {

    private int nameId;
    private byte[] bytes;
    private final Interpreter interpreter;

    public ModifyVariableData(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.nameId = stream.readInt();
        this.bytes = stream.readAllBytes();
    }
    @Override
    public void execute() throws IOException {
        var value = this.interpreter.getAlgebraicExpressionManager()
                .getResult(this.bytes);

        this.interpreter.getCurrentVariableByNameId(this.nameId)
                .setValue(value);
    }
}
