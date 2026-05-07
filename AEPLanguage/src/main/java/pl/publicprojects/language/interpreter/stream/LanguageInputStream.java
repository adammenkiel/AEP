package pl.publicprojects.language.interpreter.stream;

import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.LanguageData;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LanguageInputStream extends DataInputStream {


    /**
     * @param in Input Stream that we will work with.
     */
    public LanguageInputStream(InputStream in) {
        super(in);
    }

    /**
     * Function for reading byte[] and parsing it into LanguageData that are commands for interpreter
     *
     * @return Returns LanguageData that we read
     */
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

    /**
     * Read byte array as LanguageInputStream
     *
     * @return Returns LanguageInputStream, we can read sub instructions from it
     */
    public LanguageInputStream readLInputStream() throws IOException {
        int len = this.readInt();
        byte[] bytes = new byte[len];
        this.readFully(bytes);
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
        return new LanguageInputStream(bytesStream);
    }

    /**
     * Debug function
     */
    public String debugTable(byte[] bytes) {
        StringBuilder lol = new StringBuilder("{");
        for(byte b : bytes) {
            lol.append((int) b).append(", ");
        }
        lol.append("}");
        return lol.toString();
    }

    /**
     * Function for read byte[]
     *
     * @return array of bytes
     */
    public byte[] readBytesTable() throws IOException {
        int len = this.readInt();
        byte[] bytes = new byte[len];
        this.readFully(bytes);
        return bytes;
    }
}
