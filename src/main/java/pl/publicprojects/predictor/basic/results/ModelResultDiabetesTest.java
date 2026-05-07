package pl.publicprojects.predictor.basic.results;

import java.util.Scanner;

public class ModelResultDiabetesTest {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        while(s.hasNextLine()) {
            String lab = s.nextLine();
            String[] argsS = lab.split(" ");

            double a = Double.parseDouble(argsS[0]);
            double b = Double.parseDouble(argsS[1]);
            double c = Double.parseDouble(argsS[2]);
            double d = Double.parseDouble(argsS[3]);
            double e = Double.parseDouble(argsS[4]);
            double f = Double.parseDouble(argsS[5]);
            double g = Double.parseDouble(argsS[6]);
            double h = Double.parseDouble(argsS[7]);

            double result = (((e * (a - (3.0 * 2.0))) + (2.0 * ((((b - ((2.0 * c) + 0.0)) + ((a - (3.0 * 2.0)) + a)) * (2.0 * f)) - (0.0 * (0.0 - ((2.0 + (1.0 + (a - 2.0))) - 3.0)))))) - 0.0);
            System.out.println("Result: " + (result > 0));
        }

    }
}
