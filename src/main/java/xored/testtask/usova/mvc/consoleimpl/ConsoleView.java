package xored.testtask.usova.mvc.consoleimpl;

import xored.testtask.usova.Table;
import xored.testtask.usova.mvc.View;

public class ConsoleView implements View {
    @Override
    public void showTable(Table table) {
        System.out.println(table.toString());
    }
}
