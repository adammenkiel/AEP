package pl.adeks.language.interpreter.data.math.number.numbers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.adeks.language.interpreter.data.math.LanguageNumber;
import pl.adeks.language.interpreter.stream.LanguageInputStream;
import pl.adeks.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;

@Getter
@NoArgsConstructor
public class IntegerNumber extends LanguageNumber<Integer> {

    private Integer value;

    public IntegerNumber(int value) {
        this.value = value;
    }

    @Override
    public LanguageNumber<?> plus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new IntegerNumber(this.value + val.getValue());
            case ShortNumber val -> new IntegerNumber(this.value + val.getValue());
            case IntegerNumber val -> new IntegerNumber(this.value + val.getValue());
            case LongNumber val -> new LongNumber(this.value + val.getValue());
            case FloatNumber val -> new FloatNumber(this.value + val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value + val.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> minus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new IntegerNumber(this.value - val.getValue());
            case ShortNumber val -> new IntegerNumber(this.value - val.getValue());
            case IntegerNumber val -> new IntegerNumber(this.value - val.getValue());
            case LongNumber val -> new LongNumber(this.value - val.getValue());
            case FloatNumber val -> new FloatNumber(this.value - val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value - val.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> divide(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new IntegerNumber(this.value / val.getValue());
            case ShortNumber val -> new IntegerNumber(this.value / val.getValue());
            case IntegerNumber val -> new IntegerNumber(this.value / val.getValue());
            case LongNumber val -> new LongNumber(this.value / val.getValue());
            case FloatNumber val -> new FloatNumber(this.value / val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value / val.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> multiple(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new IntegerNumber(this.value * val.getValue());
            case ShortNumber val -> new IntegerNumber(this.value * val.getValue());
            case IntegerNumber val -> new IntegerNumber(this.value * val.getValue());
            case LongNumber val -> new LongNumber(this.value * val.getValue());
            case FloatNumber val -> new FloatNumber(this.value * val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value * val.getValue());
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
        this.value = stream.readInt();
    }

    @Override
    public void write(LanguageOutputStream stream) throws IOException {
        stream.writeInt(this.value);
    }

    @Override
    public String toString() {
        return value + "";
    }
}
