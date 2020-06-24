package xored.testtask.usova.cell;

import xored.testtask.usova.Table;

public abstract class Cell {
    protected CellState state = CellState.UNVISITED;
    protected boolean error = false;
    protected Object value;

    public abstract Object getValue(Table table);
    public abstract void init(String str);

    public CellState getState() {
        return state;
    }

    public void setError(String message) {
        error = true;
        value = "#" + message;
    }

    public boolean isError() {
        return error;
    }

    public void setNonError() {
        error = false;
        value = "";
    }
}
