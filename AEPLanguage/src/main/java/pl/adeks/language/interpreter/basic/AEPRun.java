package pl.adeks.language.interpreter.basic;


import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.stream.LanguageInputStream;

import java.io.*;

public class AEPRun {

    public static boolean TEST = true;

    public static void main(String[] args) throws IOException {
        if (!TEST) {
            if (args.length == 0) {
                System.out.println("Please type a filename.");
                return;
            }

            String fileName = args[0];
            File file = new File(fileName);
            Interpreter interpreter = new Interpreter();
            interpreter.run(new LanguageInputStream(new FileInputStream(file)));
        } else {
            String fileName = "AEPPublic/programs/five.aep";
            File file = new File(fileName);
            Interpreter interpreter = new Interpreter();
            interpreter.run(new LanguageInputStream(new FileInputStream(file)));
            System.out.println("size: " + interpreter.getCurrentVariables().size());
            System.out.println("name: " + interpreter.getCurrentVariables().get(0).getNameId());
            System.out.println("value: " + interpreter.getCurrentVariables().get(0).getValue());
        }
    }
}