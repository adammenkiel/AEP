package pl.publicprojects.aep.tests.interpreter.program;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class ProgramGenerator {

    public void saveProgram(String location) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(location);
        fileOutputStream.write(this.getProgramBytecode());
        fileOutputStream.close();
    }
    public abstract byte[] getProgramBytecode() throws IOException;
}
