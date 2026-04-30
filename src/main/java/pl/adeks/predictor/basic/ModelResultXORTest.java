package pl.adeks.predictor.basic;

import java.util.Scanner;

public class ModelResultXORTest {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        while(s.hasNextLine()) {
            String lab = s.nextLine();
            String[] argsS = lab.split(" ");

            //6 148 72 35 0 33.6 0.627 50
            double a = Double.parseDouble(argsS[0]);
            double b = Double.parseDouble(argsS[1]);
            double c = Double.parseDouble(argsS[2]);
            double d = Double.parseDouble(argsS[3]);

            double result = (((((a - b) * c) - d) / (24.0 - (((3.0 * 80.0) * (79.0 - d)) + d))) * (((((a - b) * c) - d) * ((((a - b) * c) - d) - (4.0 + 62.0))) / (84.0 + ((((((((((a - b) * c) - d) * (((a - b) * c) - d)) / 15.0) / ((c + (a - b)) * 63.0)) / 44.0) / 37.0) * (((((((a - b) * c) - d) * (((a - b) * c) - d)) / 15.0) / ((c + (a - b)) * 63.0)) / 44.0)) + 34.0))));

            System.out.println("Result: " + (result > 0));
        }

    }
}
