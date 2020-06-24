package xored.testtask.usova.cell;

import xored.testtask.usova.operations.Operation;
import xored.testtask.usova.operations.implementations.DivOperation;
import xored.testtask.usova.operations.implementations.MultOperation;
import xored.testtask.usova.operations.implementations.PlusOperation;
import xored.testtask.usova.operations.implementations.SubOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CellsInfo {
    private static String cellsPackageName;

    private static Map<String, String> cellsMapping = new HashMap<>();
    private static Map<String, Operation> operations = new HashMap<>();

    static {
        cellsPackageName = "xored.testtask.usova.cell.implementations";
        cellsMapping.put("","EmptyCell");
        cellsMapping.put("=", "ExpressionCell");
        cellsMapping.put("'", "StringCell");
        cellsMapping.put("1", "IntegerCell");
        cellsMapping.put("2", "IntegerCell");
        cellsMapping.put("3", "IntegerCell");
        cellsMapping.put("4", "IntegerCell");
        cellsMapping.put("5", "IntegerCell");
        cellsMapping.put("6", "IntegerCell");
        cellsMapping.put("7", "IntegerCell");
        cellsMapping.put("8", "IntegerCell");
        cellsMapping.put("9", "IntegerCell");

        operations.put("+", new PlusOperation());
        operations.put("-", new SubOperation());
        operations.put("/", new DivOperation());
        operations.put("*", new MultOperation());
    }

    public CellsInfo() {
        //load from file
        String operationPackageName;
        //Map<String, String> operationsMapping;

    }

    public static Operation getOperationBySymbol(String symb) {
        return operations.get(symb);
    }

    public static String getCellClassName(String key) {
        return cellsPackageName + "." + cellsMapping.get(key);
    }

    public static Set<String> getOperations() {
        return operations.keySet();
    }
}
