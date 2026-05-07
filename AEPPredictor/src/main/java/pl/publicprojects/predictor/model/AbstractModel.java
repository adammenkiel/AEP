package pl.publicprojects.predictor.model;

import java.io.IOException;

public interface AbstractModel {

    void loadData() throws Exception;

    void search() throws IOException;

    double test(byte[] bytes) throws IOException;
}
