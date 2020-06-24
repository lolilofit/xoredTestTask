package xored.testtask.usova.mvc;

import xored.testtask.usova.Table;

public class Model {
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public void evalTable(Table table) {
        table.calculate();
        view.showTable(table);
    }
}
