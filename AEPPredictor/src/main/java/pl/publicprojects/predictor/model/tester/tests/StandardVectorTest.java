package pl.publicprojects.predictor.model.tester.tests;

import lombok.Getter;
import lombok.Setter;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleVectorNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.DataLineContainer;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.tester.AbstractTester;

import java.io.IOException;
import java.util.List;

@Getter
public class StandardVectorTest implements AbstractTester<TreeVertex> {

    private final TotalDataContainer totalDataContainer;
    private final Interpreter interpreter;
    @Setter
    private List<VariableData> variables;
    private DoubleVectorNumber idx = null;

    public StandardVectorTest(
            TotalDataContainer totalDataContainer,
            Interpreter interpreter
    ) {
        this.totalDataContainer = totalDataContainer;
        this.interpreter = interpreter;
    }

    public void lag() {
        while(true) {}
    }
    @Override
    public double test(TreeVertex vert) throws IOException {

        if(this.idx == null) {
            this.idx = new DoubleVectorNumber(
                    Nd4j.zeros(((INDArray) this.totalDataContainer.getRawData()
                            .getFirst()
                            .getRawData()[0]
                            .getValue()).length())
            );
        }
        int fit = 0;
        int general = 0;

        //System.out.println("Start");
        int iter = 0;
        for (DataLineContainer info : totalDataContainer.getRawData()) {
            //System.out.println("iter " + iter);
            final INDArray correctResult = ((DoubleVectorNumber) info.getRawData()[0]).getValue().gt(0);

            info.update(this.getVariables());
            final INDArray resultVectorValue = (INDArray) vert.getValue().plus(this.idx).getValue();
            final INDArray guessResult = resultVectorValue.gt(0);
            final INDArray gradeVector = Transforms.xor(correctResult, guessResult);

            /*System.out.println("Result vec value " + resultVectorValue);
            System.out.println("guess res " + guessResult);
            System.out.println("grade vec " + gradeVector);
            */
            fit += (int) gradeVector.length() - gradeVector.castTo(DataType.INT32).sumNumber().intValue();
            general += (int) gradeVector.length();
            //System.out.println(fit + " / " + general);
            iter++;
        }
        //System.out.println("End" + ((double)fit / (double) general));
        //this.lag();
        return (double)fit / (double) general;
    }
}
