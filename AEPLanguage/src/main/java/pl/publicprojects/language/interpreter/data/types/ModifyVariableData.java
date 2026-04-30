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
        Interpreter inter = Interpreter.getInst();

        var value = inter.getAlgebraicExpressionManager()
                .getResult(this.bytes);

        inter.getCurrentVariableByNameId(this.nameId)
                .setValue(value);
    }
}
