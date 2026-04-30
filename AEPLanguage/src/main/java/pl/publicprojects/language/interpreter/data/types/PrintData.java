package pl.publicprojects.language.interpreter.data.types;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

@Getter
public class PrintData extends LanguageData {

    private int nameId;

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public void define(LanguageInputStream stream) throws IOException {
        this.nameId = stream.readInt();
    }

    @Override
    public void execute() throws IOException {
        System.out.println(
                Interpreter.getInst()
                        .getCurrentVariableByNameId(this.nameId)
                        .getValue()
        );
    }
}
