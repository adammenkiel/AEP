package pl.publicprojects.predictor.model.data.lang;

import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;

import java.io.IOException;
import java.util.List;

/**
 * Maybe I will move it into AEPLanguage in the future
 */
@Getter
public class DoubleVectorVariable extends VariableData {

    private INDArray array;

    public DoubleVectorVariable(int nameId, List<Double> doubleVector) {
        this.setNameId(nameId);
        this.array = Nd4j.create(doubleVector);
    }

    @Override
    public int getId() {
        return 101;
    }

    @Override
    public void execute() {
        Interpreter.getInst().getCurrentVariables().put(this.getNameId(), this);
    }

    @Override
    public Object getValue() {
        return new DoubleVectorNumber(array);
    }

    @Override
    public void setValue(Object obj) throws IOException {
        //System.out.println("test");
        if(obj instanceof DoubleVectorNumber number) {
            this.array = number.getValue();
            return;
        }
        throw new RuntimeException("Unsupported type " + obj.getClass().getSimpleName());
    }
}
