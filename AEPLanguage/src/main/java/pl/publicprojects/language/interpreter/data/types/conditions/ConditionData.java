package pl.publicprojects.language.interpreter.data.types.conditions;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

@Getter
public class ConditionData extends LanguageData {

    private byte[] conditionData;
    private LanguageInputStream conditionAccepted;
    private LanguageInputStream conditionFailed;

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.conditionData = stream.readBytesTable();
        this.conditionAccepted = stream.readLInputStream();
        this.conditionFailed = stream.readLInputStream();
    }

    @Override
    public void execute() throws IOException {
        final Interpreter interpreter = Interpreter.getInst();

        boolean condition = interpreter.getBooleanExpressionManager()
                .getResult(this.conditionData);

        if(condition) interpreter.start(this.conditionAccepted);
        else interpreter.start(this.conditionFailed);
    }
}
