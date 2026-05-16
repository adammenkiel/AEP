package pl.publicprojects.language.interpreter.data.types.variables.numeric;

import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.IOException;
import java.util.List;

/**
 * Class for double vector.
 */
@Getter
public class DoubleVectorVariable extends VariableData {

    private final Interpreter interpreter;
    private DoubleVectorNumber doubleVectorNumber;

    /**
     * Creates new variable.
     *
     * @param interpreter Variables are stored in Interpreter#currentVariables
     * @param nameId ID of variable
     * @param doubleVector Vector expressed as List<Double>
     */
    public DoubleVectorVariable(Interpreter interpreter, int nameId, List<Double> doubleVector) {
        this.interpreter = interpreter;
        this.setNameId(nameId);
        this.doubleVectorNumber = new DoubleVectorNumber(Nd4j.create(doubleVector));
    }

    @Override
    public int getId() {
        return 101;
    }

    @Override
    public void execute() {
        interpreter.getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        return this.doubleVectorNumber;
    }

    @Override
    public void setValue(Object obj) throws IOException {
        if(obj instanceof DoubleVectorNumber number) {
            this.doubleVectorNumber = number;
            return;
        }
        throw new RuntimeException("Unsupported type " + obj.getClass().getSimpleName());
    }
}
