package pl.publicprojects.language.interpreter.stream;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class LanguageOutputStream extends DataOutputStream {
    public LanguageOutputStream(OutputStream out) {
        super(out);
    }
}
