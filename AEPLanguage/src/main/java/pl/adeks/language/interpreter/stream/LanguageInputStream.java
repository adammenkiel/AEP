package pl.adeks.language.interpreter.stream;

import pl.adeks.language.interpreter.Interpreter;
import pl.adeks.language.interpreter.data.LanguageData;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LanguageInputStream extends DataInputStream {
    public LanguageInputStream(InputStream in) {
        super(in);
    }
    public LanguageData readLanguageData() throws IOException {
        int id = this.readInt();
        int len = this.readInt();
        final byte[] data = new byte[len];
        this.readFully(data);

        // Maybe memory leaks, need to close one
        LanguageData lData = Interpreter.getInst().getDataById(id);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        LanguageInputStream languageStream = new LanguageInputStream(dataStream);
        lData.define(languageStream);
        languageStream.close();
        dataStream.close();
        return lData;
    }

    public LanguageInputStream readLInputStream() throws IOException {
        int len = this.readInt();
        byte[] bytes = new byte[len];
        this.readFully(bytes);
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
        return new LanguageInputStream(bytesStream);
    }

    public String debugTable(byte[] bytes) {
        StringBuilder lol = new StringBuilder("{");
        for(byte b : bytes) {
            lol.append((int) b).append(", ");
        }
        lol.append("}");
        return lol.toString();
    }

    public byte[] readBytesTable() throws IOException {
        int len = this.readInt();
        byte[] bytes = new byte[len];
        this.readFully(bytes);
        return bytes;
    }
}
