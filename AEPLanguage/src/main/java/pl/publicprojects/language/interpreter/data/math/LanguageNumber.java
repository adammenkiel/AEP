package pl.publicprojects.language.interpreter.data.math;

import pl.publicprojects.language.interpreter.stream.LanguageInputStream;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;

public abstract class LanguageNumber<T> {

    public abstract T getValue();

    public abstract LanguageNumber<?> plus(LanguageNumber<?> other);
    public abstract LanguageNumber<?> minus(LanguageNumber<?> other);
    public abstract LanguageNumber<?> divide(LanguageNumber<?> other);
    public abstract LanguageNumber<?> multiple(LanguageNumber<?> other);
    public abstract LanguageNumber<?> power(LanguageNumber<?> other);
    public abstract boolean less(LanguageNumber<?> other);

    public abstract void read(LanguageInputStream stream) throws IOException;
    public abstract void write(LanguageOutputStream stream) throws IOException;

    public String toString() {
        return super.getClass().getSimpleName() +" {value = " + this.getValue() + "}";
    }

}
