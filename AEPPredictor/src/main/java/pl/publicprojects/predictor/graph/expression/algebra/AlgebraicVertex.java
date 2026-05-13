package pl.publicprojects.predictor.graph.expression.algebra;

import lombok.Getter;
import lombok.Setter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;
import pl.publicprojects.predictor.graph.TreeVertex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Getter
@Setter
public class AlgebraicVertex extends TreeVertex {

    private int operationId;
    private int index;
    private final TreeVertex[] children = new TreeVertex[2];

    private int getAlgebraicVertexType(TreeVertex vert) { //
        if(vert instanceof NumberVertex) return 0;
        if(vert instanceof VariableVertex) return 1;
        if(vert instanceof AlgebraicVertex) return 2;
        return -1;
    }

    private String getOperationString() {
        return switch (operationId) {
            case 0 -> "-";
            case 1 -> "+";
            case 2 -> "/";
            case 3 -> "*";
            case 4 -> "^";
            default -> throw new IllegalStateException("Unexpected value: " + operationId);
        };
    }

    private LanguageNumber<?> operation(LanguageNumber<?> a, LanguageNumber<?> b) {
        //System.out.println("Oper " + a.getValue() + " " + b.getValue());
        return switch (operationId) {
            case 0 -> a.minus(b);
            case 1 -> a.plus(b);
            case 2 -> a.divide(b);
            case 3 -> a.multiple(b);
            case 4 -> a.power(b);
            default -> throw new IllegalStateException("Unexpected value: " + operationId);
        };
    }

    private byte[] writeChildren(TreeVertex vert) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        LanguageOutputStream stream = new LanguageOutputStream(byteStream);
        int typeId = this.getAlgebraicVertexType(vert);
        stream.writeByte(typeId);
        byte[] bytes = vert.visit();
        if(typeId == 0 || typeId == 2) {
            stream.writeInt(bytes.length);
            stream.write(bytes);
        } else stream.write(bytes);
        
        return byteStream.toByteArray();
    }

    @Override
    public byte[] visit() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        LanguageOutputStream stream = new LanguageOutputStream(byteStream);
        stream.writeByte(operationId);
        stream.write(this.writeChildren(children[0]));
        stream.write(this.writeChildren(children[1]));
        return byteStream.toByteArray();
    }

    @Override
    public String toString() {
        return "(" + this.children[0] + " " + this.getOperationString() + " " + this.children[1] + ")";
    }

    @Override
    public LanguageNumber<?> getValue() {
        return this.operation(children[0].getValue(), children[1].getValue());
    }

    public AlgebraicVertex(int operationId) {
        this.operationId = operationId;
    }

    public void addChild(TreeVertex treeVertex) {
        children[this.index] = treeVertex;
        this.index++;
    }
    public boolean hasPlace() {
        return this.index < 2;
    }

}
