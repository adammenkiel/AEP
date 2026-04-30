package pl.publicprojects.language.interpreter.data.types.conditions;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Getter
public class WhileData extends LanguageData {

    private byte[] conditionData;
    private byte[] conditionAccepted;

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.conditionData = stream.readBytesTable();
        this.conditionAccepted = stream.readBytesTable();
    }

    @Override
    public void execute() throws IOException {
        final Interpreter interpreter = Interpreter.getInst();

        while(
                interpreter.getBooleanExpressionManager()
                    .getResult(this.conditionData)
        )
            interpreter.start(
                    new LanguageInputStream(
                            new ByteArrayInputStream(
                                    this.conditionAccepted
                            )
                    )
            );
    }
}
