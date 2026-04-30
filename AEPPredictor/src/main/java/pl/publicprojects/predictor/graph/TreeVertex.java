package pl.publicprojects.predictor.graph;

import java.io.IOException;

public abstract class TreeVertex {
    public abstract byte[] visit() throws IOException;
    public abstract String toString();
}
