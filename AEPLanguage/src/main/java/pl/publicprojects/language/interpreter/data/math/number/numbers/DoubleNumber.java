package pl.publicprojects.language.interpreter.data.math.number.numbers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;

@Getter
@NoArgsConstructor
public class DoubleNumber extends LanguageNumber<Double> {

    private Double value;

    public DoubleNumber(double value) {
        this.value = value;
    }

    @Override
    public LanguageNumber<?> plus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new DoubleNumber(this.value + val.getValue());
            case ShortNumber val -> new DoubleNumber(this.value + val.getValue());
            case IntegerNumber val -> new DoubleNumber(this.value + val.getValue());
            case LongNumber val -> new DoubleNumber(this.value + val.getValue());
            case FloatNumber val -> new DoubleNumber(this.value + val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value + val.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> minus(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new DoubleNumber(this.value - val.getValue());
            case ShortNumber val -> new DoubleNumber(this.value - val.getValue());
            case IntegerNumber val -> new DoubleNumber(this.value - val.getValue());
            case LongNumber val -> new DoubleNumber(this.value - val.getValue());
            case FloatNumber val -> new DoubleNumber(this.value - val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value - val.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> divide(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new DoubleNumber(this.value / val.getValue());
            case ShortNumber val -> new DoubleNumber(this.value / val.getValue());
            case IntegerNumber val -> new DoubleNumber(this.value / val.getValue());
            case LongNumber val -> new DoubleNumber(this.value / val.getValue());
            case FloatNumber val -> new DoubleNumber(this.value / val.getValue());
            case DoubleNumber val -> new DoubleNumber(this.value / val.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + other);
        };
    }

    @Override
    public LanguageNumber<?> multiple(LanguageNumber<?> other) {
        return switch (other) {
            case ByteNumber val -> new DoubleNumber(this.value * val.getValue());
            case ShortNumber val -> new DoubleNumber(this.value * val.getValue());
            case IntegerNumber val -> new DoubleNumber(this.value * val.getValue());
            case LongNumber val -> new DoubleNumber(this.value * val.getValue());
            case FloatNumber val -> new DoubleNumber(this.value * val.getValue());
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
        this.value = stream.readDouble();
    }

    @Override
    public void write(LanguageOutputStream stream) throws IOException {
        stream.writeDouble(this.value);
    }

    @Override
    public String toString() {
        return value + "";
    }
}
