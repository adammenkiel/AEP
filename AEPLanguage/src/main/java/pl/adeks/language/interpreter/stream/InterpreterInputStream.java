package pl.adeks.language.interpreter.stream;

import lombok.Getter;
import pl.adeks.language.interpreter.Interpreter;

import java.io.InputStream;

@Getter
public class InterpreterInputStream extends LanguageInputStream{

    private final Interpreter interpreter;

    public InterpreterInputStream(Interpreter interpreter, InputStream in) {
        super(in);
        this.interpreter = interpreter;
    }
}
