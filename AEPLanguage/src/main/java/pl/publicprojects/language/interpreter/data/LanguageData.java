package pl.publicprojects.language.interpreter.data;

import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

public abstract class LanguageData implements Cloneable {

    public abstract int getId();
    public abstract void define(LanguageInputStream stream) throws IOException;
    public abstract void execute() throws IOException;

    public void run() throws IOException {
        this.execute();
    }

    @Override
    public LanguageData clone() {
        try {
            return (LanguageData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
