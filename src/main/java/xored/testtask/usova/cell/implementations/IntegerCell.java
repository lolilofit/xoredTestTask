package xored.testtask.usova.cell.implementations;

import xored.testtask.usova.Table;
import xored.testtask.usova.cell.Cell;
import xored.testtask.usova.cell.CellState;

public class IntegerCell extends Cell {

    @Override
    public Object getValue(Table table) {
        state = CellState.VISITED;
        return value;
    }

    @Override
    public void init(String value) {
        try {
            super.value = Integer.parseInt(value);
        }
        catch (NumberFormatException  e) {
            setError("ErrorType");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
