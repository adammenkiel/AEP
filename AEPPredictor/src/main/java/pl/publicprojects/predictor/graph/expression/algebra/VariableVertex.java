package pl.publicprojects.predictor.graph.expression.algebra;

import lombok.Getter;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;
import pl.publicprojects.predictor.graph.TreeVertex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Getter
public class VariableVertex extends TreeVertex {

    private int nameId;

    public VariableVertex(int nameId) {
        this.nameId = nameId;
    }

    public VariableVertex(VariableData data) {
        this.nameId = data.getNameId();
    }

    @Override
    public byte[] visit() throws IOException {
        ByteArrayOutputStream streamBytes = new ByteArrayOutputStream();
        LanguageOutputStream stream = new LanguageOutputStream(streamBytes);
        stream.writeInt(nameId);
        return streamBytes.toByteArray();
    }

    @Override
    public String toString() {
        return "$" + nameId + "$";
    }
}
