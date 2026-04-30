package pl.publicprojects.language.interpreter;

import lombok.Getter;
import pl.publicprojects.language.interpreter.data.LanguageData;
import pl.publicprojects.language.interpreter.data.math.bool.BooleanExpressionManager;
import pl.publicprojects.language.interpreter.data.math.number.AlgebraicExpressionManager;
import pl.publicprojects.language.interpreter.data.types.PrintData;
import pl.publicprojects.language.interpreter.data.types.conditions.ConditionData;
import pl.publicprojects.language.interpreter.data.types.ModifyVariableData;
import pl.publicprojects.language.interpreter.data.types.VariableData;
import pl.publicprojects.language.interpreter.data.types.conditions.WhileData;
import pl.publicprojects.language.interpreter.data.types.variables.bool.BooleanVariable;
import pl.adeks.language.interpreter.data.types.variables.numeric.*;
import pl.publicproject.language.interpreter.data.types.variables.numeric.*;
import pl.publicprojects.language.interpreter.data.types.variables.numeric.*;
import pl.publicprojects.language.interpreter.stream.LanguageInputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Interpreter {

    @Getter
    @Deprecated
    private static Interpreter inst;

    private final Map<Integer, LanguageData> dataMap = new HashMap<>(); // Global data list

    private final Map<Integer, VariableData> variableTypes = new HashMap<>(); // maybe will be deleted
    private final Map<Integer, VariableData> currentVariables = new HashMap<>();

    private final AlgebraicExpressionManager algebraicExpressionManager;
    private final BooleanExpressionManager booleanExpressionManager;

    public Interpreter() {
        inst = this; // Temp. option
        this.algebraicExpressionManager = new AlgebraicExpressionManager(this);
        this.booleanExpressionManager = new BooleanExpressionManager(this);
    }

    public void registerData(LanguageData data) {
        this.dataMap.put(data.getId(), data);
        if(data instanceof VariableData variableData) {
            this.variableTypes.put(variableData.getId(), variableData);
        }
    }
    public void registerData(LanguageData... dataList) {
        for(LanguageData data : dataList) {
            System.out.println("Registered LanguageData {opcode = " + data.getId() + "} " + data.getClass().getSimpleName());
        }
        Arrays.asList(dataList).forEach(this::registerData);
    }

    public LanguageData getDataById(int id) {
        return this.dataMap.get(id).clone();
    }

    public void run(final LanguageInputStream stream) throws IOException {
        this.registerData(
                new ByteVariable(),
                new ShortVariable(),
                new IntegerVariable(),
                new LongVariable(),
                new FloatVariable(),
                new DoubleVariable(),
                new BooleanVariable(),
                new ModifyVariableData(),
                new ConditionData(),
                new WhileData(),
                new PrintData()
        );
        this.start(stream);
    }

    public void start(final LanguageInputStream stream) throws IOException {
        while(stream.available() > 0) {
            stream.readLanguageData().run();
        }
        stream.close();
    }
    public void createCurrentVariable(VariableData data) {
        this.currentVariables.put(data.getNameId(), data);
    }

    public VariableData getCurrentVariableByNameId(int nameId) {
        return this.currentVariables.get(nameId);
    }
}
