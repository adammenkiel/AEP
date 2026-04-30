package pl.publicprojects.language.interpreter.data.types;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

@Getter
@Setter
public abstract class VariableData extends LanguageData {

    private int nameId;
    @Setter
    private byte[] data;
    private boolean executed = false;

    @Override
    public abstract int getId();

    public VariableData() {}

    public VariableData(int nameId) {
        this.nameId = nameId;
    }

    @Override
    public void define(final LanguageInputStream stream) throws IOException {
        this.nameId = stream.readInt();
        this.data = stream.readAllBytes();
    }

    public abstract void execute();

    public abstract Object getValue();

    public abstract void setValue(Object obj) throws IOException;
}
