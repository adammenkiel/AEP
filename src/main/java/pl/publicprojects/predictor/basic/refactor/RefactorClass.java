package pl.publicprojects.predictor.basic.refactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * When you run model from <code>/examples</code> folder, you can put output here and write <code>end</code>, expressions from output will be converted into one expression
 * Next you can copy it and replace <code>$0$, $1$, ..., $n$</code> into variable names like in <code>/results</code> folder
 * Sometimes expressions could look complicated, we don't simplify it because when we use it freeze setting
 * We don't need to calculate it every time when we use it as data like in <code>PoolESModel</code> and models like it
 * In the future I will do experiment with simplify methods, surely it should be calculated for good-graded results.
 */
public class RefactorClass {
    public static void main(String[] args) {
        System.out.println("Running... Please type output of Model Example from /examples/ and write `end` to get the result expression.");

        Scanner s = new Scanner(System.in);
        List<String[]> lines = new ArrayList<>();
        while(s.hasNextLine()) {
            String abc = s.nextLine();
            if(abc.equalsIgnoreCase("end")) break;
            abc = abc.substring(abc.indexOf(" - ") + 3); // remove logger message
            if(!abc.contains(" = ")) continue;
            lines.add(abc.split(" = "));
        }

        for(int i = 0; i < lines.size(); i++) {
            String[] lab = lines.get(i);

            String vName = lab[0];
            String exp = lab[1];

            for(int j = i; j < lines.size(); j++) {
                String[] foreign = lines.get(j);
                foreign[1] = foreign[1].replace(vName, exp);
            }
        }
        System.out.println(lines.getLast()[1]);
    }
}
