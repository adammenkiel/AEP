package pl.publicprojects.predictor.graph;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;

import java.io.IOException;

/**
 * TreeVertex is a part of expression graph that is generating at ExpressGraphGenerator
 */
public abstract class TreeVertex {
    /**
     * @return Returns expression bytecode in byte[] form
     */
    public abstract byte[] visit() throws IOException;

    /**
     * @return Returns expression in String form
     */
    public abstract String toString();

    /**
     * Evaluates expression result without AlgebraicExpressionManager
     * @param interpreter Loads variable values
     * @return Returns expression result
     */
    public abstract LanguageNumber<?> getValue(Interpreter interpreter);
}
