package xored.testtask.usova.operations;

public interface Operation {
    Integer eval(Integer... params);
    Integer getOperandsNum();
    Integer getPrior();
}
