package pl.publicprojects.language.interpreter.stream;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class LanguageOutputStream extends DataOutputStream {
    /**
     * Creates a new data output stream to write data to the specified
     * underlying output stream. The counter {@code written} is
     * set to zero.
     *
     * @param out the underlying output stream, to be saved for later
     *            use.
     * @see FilterOutputStream#out
     */
    public LanguageOutputStream(OutputStream out) {
        super(out);
    }
}
