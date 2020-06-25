package xored.testtask.usova.cell.implementations;

import xored.testtask.usova.Table;
import xored.testtask.usova.cell.Cell;

//cell with nothing
public class EmptyCell extends Cell {

    @Override
    public Object getValue(Table table) {
        return value;
    }

    @Override
    public void init(String value) {
        super.value = null;
    }

    @Override
    public String toString() {
        return "";
    }
}
