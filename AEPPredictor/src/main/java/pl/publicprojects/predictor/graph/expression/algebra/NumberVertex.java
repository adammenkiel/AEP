package pl.publicprojects.predictor.graph.expression.algebra;

import lombok.Getter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.*;
import pl.publicprojects.language.interpreter.stream.LanguageOutputStream;
import pl.publicprojects.predictor.graph.TreeVertex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Getter
public class NumberVertex extends TreeVertex {

    private LanguageNumber<?> number;

    public NumberVertex(LanguageNumber<?> number) {
        this.number = number;
    }

    public int getIdByLangNumber(LanguageNumber<?> lang) {
        if(lang instanceof ByteNumber) return 0;
        if(lang instanceof ShortNumber) return 1;
        if(lang instanceof IntegerNumber) return 2;
        if(lang instanceof LongNumber) return 3;
        if(lang instanceof FloatNumber) return 4;
        if(lang instanceof DoubleNumber) return 5;
        return -1;
    }

    @Override
    public byte[] visit() throws IOException {
        ByteArrayOutputStream numberDataByte = new ByteArrayOutputStream();
        LanguageOutputStream numberData = new LanguageOutputStream(numberDataByte);
        LanguageNumber<?> lang = this.getNumber();
        numberData.writeByte(this.getIdByLangNumber(lang));
        lang.write(numberData);
        return numberDataByte.toByteArray();
    }

    @Override
    public String toString() {
        return this.number.getValue().toString();
    }

    @Override
    public LanguageNumber<?> getValue() {
        return this.number;
    }
}
