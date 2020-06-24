package xored.testtask.usova.mvc;

import xored.testtask.usova.Table;

public interface Controller {
    Table readTable();
    void setModel(Model model);
}
