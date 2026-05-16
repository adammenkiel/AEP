package pl.publicprojects.language.interpreter.data.math;

import pl.publicprojects.language.interpreter.stream.LanguageInputStream;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;

import java.io.IOException;


/**
 * We need this class as Number from JDK doesn't support algebraic expressions and Vectors is also a reason.
 * @param <T> Parameter for objects that possible to do single math operations.
 */
public abstract class LanguageNumber<T> {

    /**
     * @return Returns object integer value
     */
    public abstract T getValue();

    /**
     * We calculate: this + other
     * @param other Other number
     * @return Result
     */
    public abstract LanguageNumber<?> plus(LanguageNumber<?> other);

    /**
     * We calculate: this - other
     * @param other Other number
     * @return Result
     */
    public abstract LanguageNumber<?> minus(LanguageNumber<?> other);

    /**
     * We calculate: this / other
     * @param other Other number
     * @return Result
     */
    public abstract LanguageNumber<?> divide(LanguageNumber<?> other);

    /**
     * We calculate: this * other
     * @param other Other number
     * @return Result
     */
    public abstract LanguageNumber<?> multiple(LanguageNumber<?> other);

    /**
     * We calculate: this ^ other
     * @param other Other number
     * @return Result
     */
    public abstract LanguageNumber<?> power(LanguageNumber<?> other);

    /**
     * We calculate: this < other
     * @param other Other number
     * @return Result
     */
    public abstract boolean less(LanguageNumber<?> other);

    /**
     * Method for read number from byte[] form
     *
     * @param stream Bytes of program
     * @throws IOException Throws when program have errors in syntax etc...
     */
    public abstract void read(LanguageInputStream stream) throws IOException;

    /**
     * Method for write number to stream
     *
     * @param stream stream to write, if that's ByteArrayOutputStream, we can get byte[] by this way
     * @throws IOException Throws when program have errors in syntax etc...
     */
    public abstract void write(LanguageOutputStream stream) throws IOException;


    public String toString() {
        return super.getClass().getSimpleName() +" {value = " + this.getValue() + "}";
    }

}
