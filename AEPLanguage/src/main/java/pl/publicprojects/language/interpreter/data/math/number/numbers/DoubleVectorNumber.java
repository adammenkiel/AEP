package pl.publicprojects.language.interpreter.data.math.number.numbers;

import lombok.NoArgsConstructor;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class DoubleVectorNumber extends LanguageNumber<INDArray> {

    private INDArray value;

    public DoubleVectorNumber(INDArray value) {
        this.value = value;
    }
    public DoubleVectorNumber(List<Double> doubles) {
        this.value = Nd4j.create(doubles);
    }

    @Override
    public INDArray getValue() {
        return this.value;
    }

    @Override
    public LanguageNumber<?> plus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            case ShortNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            case IntegerNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            case LongNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            case FloatNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            case DoubleNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            case DoubleVectorNumber val -> new DoubleVectorNumber(this.value.add(val.getValue()));
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> minus(LanguageNumber<?> other) {
        if(other.getValue() instanceof Number val) {
            return new DoubleVectorNumber(this.value.sub(val));
        } else if(other.getValue() instanceof INDArray arr) {
            return new DoubleVectorNumber(this.value.sub(arr));
        }
        throw new IllegalStateException("Unexpected value: " + other);
    }

    @Override
    public LanguageNumber<?> divide(LanguageNumber<?> other) {
        if(other.getValue() instanceof Number val) {
            return new DoubleVectorNumber(this.value.div(val));
        } else if(other.getValue() instanceof INDArray arr) {
            return new DoubleVectorNumber(this.value.div(arr));
        }
        throw new IllegalStateException("Unexpected value: " + other);
    }

    @Override
    public LanguageNumber<?> multiple(LanguageNumber<?> other) {
        if(other.getValue() instanceof Number val) {
            return new DoubleVectorNumber(this.value.mul(val));
        } else if(other.getValue() instanceof INDArray arr) {
            return new DoubleVectorNumber(this.value.mul(arr));
        }
        throw new IllegalStateException("Unexpected value: " + other);
    }

    @Override
    public LanguageNumber<?> power(LanguageNumber<?> other) {
        if(other.getValue() instanceof Number val) {
            return new DoubleVectorNumber(Transforms.pow(this.value, val));
        } else if(other.getValue() instanceof INDArray arr) {
            return new DoubleVectorNumber(Transforms.pow(this.value, arr));
        }
        throw new IllegalStateException("Unexpected value: " + other);
    }

    @Override
    public boolean less(LanguageNumber<?> other) {
        throw new RuntimeException("Unsupported function");
    }

    /* @Override
    public INDArray less(LanguageNumber<?> other) {
        if(other.getValue() instanceof Number val) {
            return this.value.lt(val);
        } else if(other.getValue() instanceof INDArray arr) {
            return this.value.lt(arr);
        }
        throw new IllegalStateException("Unexpected value: " + other);
    }*/

    /**
     * For correct
     */
    @Override
    public void read(LanguageInputStream stream) throws IOException {
        double[] doubleArr = new double[stream.readInt()];
        for(int i = 0; i < doubleArr.length; i++) {
            doubleArr[i] = stream.readDouble();
        }
        this.value = Nd4j.create(doubleArr);
    }

    @Override
    public void write(LanguageOutputStream stream) throws IOException {
        long len = this.value.length();
        stream.writeInt((int)len);
        for(int i = 0; i < len; i++) {
            stream.writeDouble(this.value.getDouble(i));
        }
    }
}
