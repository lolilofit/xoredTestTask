package xored.testtask.usova;

import xored.testtask.usova.mvc.Controller;
import xored.testtask.usova.mvc.Model;
import xored.testtask.usova.mvc.View;
import xored.testtask.usova.mvc.consoleimpl.ConsoleController;
import xored.testtask.usova.mvc.consoleimpl.ConsoleView;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new ConsoleView();
        Controller controller = new ConsoleController();

        model.setView(view);
        controller.setModel(model);

        controller.readTable();
    }
}
