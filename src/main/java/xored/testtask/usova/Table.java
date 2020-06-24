package xored.testtask.usova;

import xored.testtask.usova.cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table {
    private int width;
    private int heigh;
    private List<Cell> cells;


    public Table(int width, int heigh) {
        this.width = width;
        this.heigh = heigh;

        cells = new ArrayList<>();
    }

    //refactor!!!
    public void addCell(int coord, Cell cell) {
        cells.add(cell);
    }

    public Integer convertCellNumber(String coords) {
        Pattern pattern = Pattern.compile("[A-Za-z][0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(coords);

        if(!matcher.matches())
            return -1;

        char firstChar = coords.toLowerCase().charAt(0);
        int x = (int)firstChar - (int)('a');
        int y = Integer.parseInt(coords.substring(1, 2)) - 1;

        return y*width + x;
    }

    public Cell getCell(int coords) {
        return cells.get(coords);
    }

    public void calculate() {
        Cell cell;

        for(int i = 0; i < width*heigh; i++) {
            cell = cells.get(i);
            if (cell != null)
                cell.getValue(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < heigh; i++) {
            for(int j = 0; j < width; j++) {
                result.append(cells.get(i*width + j).toString()).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
