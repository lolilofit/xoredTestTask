package xored.testtask.usova.cell.implementations;

import xored.testtask.usova.Table;
import xored.testtask.usova.cell.Cell;

//cell with string value
public class StringCell extends Cell {
    @Override
    public Object getValue(Table table) {
        return value;
    }

    @Override
    public void init(String value) {
        super.value = value.substring(1);
    }

    @Override
    public String toString() {
        if(!error)
            return "'" + value.toString();
        return value.toString();
    }
}
