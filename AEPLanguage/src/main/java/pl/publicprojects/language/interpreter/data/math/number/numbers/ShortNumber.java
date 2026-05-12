package pl.publicprojects.language.interpreter.data.math.number.numbers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;

@Getter
@NoArgsConstructor
public class ShortNumber extends LanguageNumber<Short> {

    private Short value;

    public ShortNumber(short value) {
        this.value = value;
    }

    @Override
    public LanguageNumber<?> plus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new ShortNumber((short) (this.value + val.getValue()));
            case ShortNumber val -> new ShortNumber((short) (this.value + val.getValue()));
            case IntegerNumber val -> new IntegerNumber(this.value + val.getValue());
            case LongNumber val -> new LongNumber(this.value + val.getValue());
            case FloatNumber val -> new FloatNumber(this.value + val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value + val.getValue());
            case DoubleVectorNumber val -> new DoubleVectorNumber(val.getValue().add(this.value));
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> minus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new ShortNumber((short) (this.value - val.getValue()));
            case ShortNumber val -> new ShortNumber((short) (this.value - val.getValue()));
            case IntegerNumber val -> new IntegerNumber(this.value - val.getValue());
            case LongNumber val -> new LongNumber(this.value - val.getValue());
            case FloatNumber val -> new FloatNumber(this.value - val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value - val.getValue());
            case DoubleVectorNumber val -> new DoubleVectorNumber(val.getValue().rsub(this.value));
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> divide(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new ShortNumber((short) (this.value / val.getValue()));
            case ShortNumber val -> new ShortNumber((short) (this.value / val.getValue()));
            case IntegerNumber val -> new IntegerNumber(this.value / val.getValue());
            case LongNumber val -> new LongNumber(this.value / val.getValue());
            case FloatNumber val -> new FloatNumber(this.value / val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value / val.getValue());
            case DoubleVectorNumber val -> new DoubleVectorNumber(val.getValue().rdiv(this.value));
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> multiple(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new ShortNumber((short) (this.value * val.getValue()));
            case ShortNumber val -> new ShortNumber((short) (this.value * val.getValue()));
            case IntegerNumber val -> new IntegerNumber(this.value * val.getValue());
            case LongNumber val -> new LongNumber(this.value * val.getValue());
            case FloatNumber val -> new FloatNumber(this.value * val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value * val.getValue());
            case DoubleVectorNumber val -> new DoubleVectorNumber(val.getValue().mul(this.value));
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> power(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new DoubleNumber(Math.pow(this.value, val.getValue()));
            case ShortNumber val -> new DoubleNumber(Math.pow(this.value, val.getValue()));
            case IntegerNumber val -> new DoubleNumber(Math.pow(this.value, val.getValue()));
            case LongNumber val -> new DoubleNumber(Math.pow(this.value, val.getValue()));
            case FloatNumber val -> new DoubleNumber(Math.pow(this.value, val.getValue()));
            case DoubleNumber val -> new DoubleNumber(Math.pow(this.value, val.getValue()));
            case DoubleVectorNumber val -> new DoubleVectorNumber(Transforms.pow(Nd4j.valueArrayOf(val.getValue().shape(), this.value), val.getValue()));
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public boolean less(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> this.value < val.getValue();
            case ShortNumber val -> this.value < val.getValue();
            case IntegerNumber val -> this.value < val.getValue();
            case LongNumber val -> this.value < val.getValue();
            case FloatNumber val -> this.value < val.getValue();
            case DoubleNumber val -> this.value < val.getValue();
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public void read(LanguageInputStream stream) throws IOException {
        this.value = stream.readShort();
    }

    @Override
    public void write(LanguageOutputStream stream) throws IOException {
        stream.writeShort(value);
    }

    @Override
    public String toString() {
        return value + "";
    }
}
