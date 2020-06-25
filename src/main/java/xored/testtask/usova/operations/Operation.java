package xored.testtask.usova.operations;

//Simple operation like plus and etc.
public interface Operation {
    Integer eval(Integer... params) throws OperationException;
    Integer getOperandsNum();
    Integer getPrior();
}
