package pl.publicprojects.language.interpreter.data.types;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

/**
 * Universal variable class
 */
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

    /**
     * Reads nameId of variable and byte array of value
     *
     * @param stream We need stream for load settings of this instructions
     * @throws IOException
     */
    @Override
    public void define(final LanguageInputStream stream) throws IOException {
        this.nameId = stream.readInt();
        this.data = stream.readAllBytes();
    }

    /**
     * Register this value in interpreter
     */
    public abstract void execute();

    /**
     * @return Returns value of this variable
     */
    public abstract Object getValue();

    /**
     * Sets value of variable.
     * @param obj Value, the most often expressed as LanguageNumber<?>
     * @throws RuntimeException When function doesn't support this type of variable
     */
    public abstract void setValue(Object obj) throws IOException;
}
