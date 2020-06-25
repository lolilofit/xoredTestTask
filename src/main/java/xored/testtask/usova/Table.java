package xored.testtask.usova;

import xored.testtask.usova.cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table {
    private int width;
    private int heigh;
    //list of cells with size width*heigh
    private List<Cell> cells;


    public Table(int width, int heigh) {
        this.width = width;
        this.heigh = heigh;

        cells = new ArrayList<>();
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    //convert number from format [A-Za-z][0-9] to number format (example - from B1 to 1)
    public Integer convertCellNumber(String coords) {
        Pattern pattern = Pattern.compile("[A-Za-z][0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(coords);

        //check if given string have a right form
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

    //calculate value of all cells
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
        Object cellResult;

        for(int i = 0; i < heigh; i++) {
            for(int j = 0; j < width; j++) {
                cellResult = cells.get(i*width + j).toString();
                if(cellResult != null)
                    result.append(cellResult.toString());
                result.append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
