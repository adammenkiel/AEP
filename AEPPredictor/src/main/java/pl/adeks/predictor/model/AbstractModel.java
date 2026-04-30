package pl.adeks.predictor.model;

import java.io.IOException;

public abstract class AbstractModel {

    public abstract void loadData() throws Exception;

    public abstract void search() throws IOException;

    public abstract double test(byte[] bytes) throws IOException;
}
