package pl.publicprojects.predictor.graph;

import pl.publicprojects.language.interpreter.data.math.LanguageNumber;

import java.io.IOException;

public abstract class TreeVertex {
    public abstract byte[] visit() throws IOException;
    public abstract String toString();
    public abstract LanguageNumber<?> getValue();
}
