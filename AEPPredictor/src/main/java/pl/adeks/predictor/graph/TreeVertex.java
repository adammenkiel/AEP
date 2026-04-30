package pl.adeks.predictor.graph;

import pl.adeks.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;

public abstract class TreeVertex {
    public abstract byte[] visit() throws IOException;
    public abstract String toString();
}
