package xored.testtask.usova.cell;

import xored.testtask.usova.cell.implementations.EmptyCell;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class CellFabric {
    private static Logger logger = Logger.getLogger(CellFabric.class.getName());

    public Cell getCell(String firstSymbol, String value) {
        Cell cell;
        try {
            String className = CellsInfo.getCellClassName(firstSymbol);
            Class<?> cellClass = Class.forName(className);
            cell = (Cell) cellClass.getDeclaredConstructor().newInstance();
            cell.init(value);

            return cell;
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            cell = new EmptyCell();
            cell.setError("CreationError");

            logger.info(e.getMessage());
        }
        return cell;
    }
}
