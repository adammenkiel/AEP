package pl.publicprojects.language.interpreter.stream;

import lombok.Getter;
import pl.publicprojects.language.interpreter.Interpreter;

import java.io.InputStream;

@Deprecated
@Getter
public class InterpreterInputStream extends LanguageInputStream{

    private final Interpreter interpreter;

    public InterpreterInputStream(Interpreter interpreter, InputStream in) {
        super(in);
        this.interpreter = interpreter;
    }
}
