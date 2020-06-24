package xored.testtask.usova.operations.implementations;

import xored.testtask.usova.operations.Operation;

public class PlusOperation implements Operation {
    private static final Integer OPERANDS_NUM = 2;
    private static final Integer PRIOR = 1;

    @Override
    public Integer eval(Integer... params) {
        if(params.length != OPERANDS_NUM)
            return null;
        return params[0] + params[1];
    }

    @Override
    public Integer getOperandsNum() {
        return OPERANDS_NUM;
    }

    @Override
    public Integer getPrior() {
        return PRIOR;
    }
}
