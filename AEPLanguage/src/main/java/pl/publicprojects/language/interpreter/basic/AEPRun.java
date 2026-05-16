package pl.publicprojects.language.interpreter.basic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.*;

public class AEPRun {

    private static final Logger logger = LoggerFactory.getLogger(AEPRun.class);

    /**
     * Main class for AEPInterpreter
     * @param args Filename, default format .aep
     * @throws IOException If something wrong happens (for example program not work)
     */
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            logger.info("Please type a filename.");
            return;
        }

        String fileName = args[0];
        File file = new File(fileName);
        Interpreter interpreter = new Interpreter();
        interpreter.run(new LanguageInputStream(interpreter, new FileInputStream(file)));
    }
}