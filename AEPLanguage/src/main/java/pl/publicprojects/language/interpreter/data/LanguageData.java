package pl.publicprojects.language.interpreter.data;

import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;

/**
 * Atoms of this language
 */
public abstract class LanguageData implements Cloneable {

    /**
     * @return Returns instruction's opcode/id
     */
    public abstract int getId();

    /**
     * Function for preload an instruction, here we can read byte data about this instruction
     *
     * @param stream We need stream for load settings of this instructions
     */
    public abstract void define(LanguageInputStream stream) throws IOException;

    /**
     * Function to execute an instruction when we need to do it/
     */
    public abstract void execute() throws IOException;

    public void run() throws IOException {
        this.execute();
    }

    /**
     * If we want to make it work async, we need to clone it when we will get data by id.
     * Now Interpreter isn't thread safe yet because we use Interpreter#getInst
     */
    @Override
    public LanguageData clone() {
        try {
            return (LanguageData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
