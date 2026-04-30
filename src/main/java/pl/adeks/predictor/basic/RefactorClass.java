package pl.adeks.predictor.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RefactorClass {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        List<String[]> lines = new ArrayList<>();
        while(s.hasNextLine()) {
            String abc = s.nextLine();
            if(abc.equalsIgnoreCase("end")) break;
            if(!abc.contains(" = ")) continue;
            lines.add(abc.split(" = "));
        }
        for(int i = 0; i < lines.size(); i++) {
            String[] lab = lines.get(i);

            String vName = lab[0];
            String exp = lab[1];

            for(int j = i; j < lines.size(); j++) {
                String[] foreign = lines.get(j);
                //System.out.println("before" + foreign[1]);
                //System.out.println(foreign[0] + " replaceAll " + vName );
                foreign[1] = foreign[1].replace(vName, exp);
                //System.out.println(foreign[1]);
            }
        }
        System.out.println(lines.getLast()[1]);
    }
}
