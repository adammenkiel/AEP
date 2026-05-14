package pl.publicprojects.language.interpreter.basic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.*;

public class AEPRun {

    public static boolean TEST = true;

    private static final Logger logger = LoggerFactory.getLogger(AEPRun.class);

    public static void main(String[] args) throws IOException {
        if (!TEST) {
            if (args.length == 0) {
                logger.info("Please type a filename.");
                return;
            }

            String fileName = args[0];
            File file = new File(fileName);
            Interpreter interpreter = new Interpreter();
            interpreter.run(new LanguageInputStream(interpreter, new FileInputStream(file)));
        } else {
            String fileName = "AEPPublic/programs/five.aep";
            File file = new File(fileName);
            Interpreter interpreter = new Interpreter();
            interpreter.run(new LanguageInputStream(interpreter, new FileInputStream(file)));
            logger.info("size: {}", interpreter.getCurrentVariables().size());
            logger.info("name: {}", interpreter.getCurrentVariables().get(0).getNameId());
            logger.info("value: {}", interpreter.getCurrentVariables().get(0).getValue());
        }
    }
}