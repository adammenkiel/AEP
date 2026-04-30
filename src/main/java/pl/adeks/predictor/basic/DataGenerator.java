package pl.adeks.predictor.basic;

import java.util.Random;

public class DataGenerator {

    public static void simpleDataVectorGenerator(int a) {
        Random r = new Random();
        for(int i = 0; i < a; i++) {
            int aV = r.nextInt(2);
            int bV = r.nextInt(2);
            int cV = r.nextInt(2);
            int dV = r.nextInt(2);
            int res = 1 - (aV + bV + cV + dV) % 2;
            System.out.println(res + " " + aV + " " + bV + " " + cV + " " + dV);
        }
    }
    public static void randomTestGenerator(int a) {
        Random r = new Random();
        for(int i = 0; i < a; i++) {
            int aV = r.nextInt(2);
            int bV = r.nextInt(2);
            int cV = r.nextInt(2);
            int dV = r.nextInt(2);
            int eV = r.nextInt(2);
            int fV = r.nextInt(2);
            int gV = r.nextInt(2);
            int hV = r.nextInt(2);
            int iV = r.nextInt(2);
            int res = r.nextInt(2);
            System.out.println(res + " " + aV + " " + bV + " " + cV + " " + dV + " " + eV + " " + fV + " " + gV + " " + hV + " " + iV);
        }
    }

    public static void main(String[] args) {
        randomTestGenerator(100);
    }
}
