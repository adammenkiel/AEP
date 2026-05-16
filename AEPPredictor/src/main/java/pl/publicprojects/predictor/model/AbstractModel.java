package pl.publicprojects.predictor.model;

import java.io.IOException;

public interface AbstractModel {

    /**
     * Implements data loading way, shared for users to load data
     *
     * @throws Exception When file not found and another cases
     */
    void loadData() throws Exception;

    /**
     * Implements function searches a solution with the best fitness for data
     */
    void search() throws IOException;

    /**
     * Function is unsupported now, it's considering to add bytecode inlining from AbstractTester
     * in next updates for improve efficiency
     *
     * @return Returns result of test
     */
    double test(byte[] bytes) throws IOException;
}
