package xored.testtask.usova.mvc.consoleimpl;

import xored.testtask.usova.Table;
import xored.testtask.usova.cell.Cell;
import xored.testtask.usova.cell.CellFabric;
import xored.testtask.usova.mvc.Controller;
import xored.testtask.usova.mvc.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleController implements Controller {
    private Model model;
    private CellFabric cellFabric = new CellFabric();

    @Override
    public Table readTable() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String line;
        int height = 0;
        int width = 0;

        try {
            line = in.readLine();

            String[] splittedLine = line.split("\t");
            height = Integer.parseInt(splittedLine[0]);
            width = Integer.parseInt(splittedLine[1]);

            Table table = new Table(width, height);
            Cell cell;

            if(height <= 0 || width <= 0)
                return null;

            for(int i = 0; i < height; i++) {
                line = in.readLine();
                String[] tableLine = line.split("\t");

                for(int j = 0; j < tableLine.length; j++) {
                    line = tableLine[j];
                    cell = cellFabric.getCell(line.substring(0, 1), line);
                    table.addCell(i*width + j, cell);
                }
            }

            model.evalTable(table);

            return table;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }
}
