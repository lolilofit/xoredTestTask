package xored.testtask.usova.cell;

import xored.testtask.usova.operations.Operation;
import xored.testtask.usova.operations.implementations.DivOperation;
import xored.testtask.usova.operations.implementations.MultOperation;
import xored.testtask.usova.operations.implementations.PlusOperation;
import xored.testtask.usova.operations.implementations.SubOperation;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellsInfo {
    private static String cellsPackageName;

    private static Map<String, String> cellsMapping = new HashMap<>();
    private static Map<String, Operation> operations = new HashMap<>();

    private static Logger logger = Logger.getLogger(CellsInfo.class.getName());

    static {
        cellsPackageName = loadPropertiesMap("src/main/resources/cell.properties", cellsMapping);

        Map<String, String> operationClassesName = new HashMap<>();
        String operationPackageName = loadPropertiesMap("src/main/resources/operation.properties", operationClassesName);

        try {
            for(Map.Entry<String, String> entry : operationClassesName.entrySet()) {
                Class<?> loadedClass = Class.forName(operationPackageName + "." + entry.getValue());
                operations.put(entry.getKey(), (Operation) loadedClass.getDeclaredConstructor().newInstance());
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            logger.info(e.getMessage());
        }
    }

    private static String loadPropertiesMap(String filename, Map<String, String> loadingInfo) {
        FileInputStream in;
        Properties property = new Properties();
        String packageName = "";

        try {
            in = new FileInputStream(filename);
            property.load(in);

            for(Map.Entry<Object, Object> entry : property.entrySet()) {
                if(entry.getKey().equals("packageName"))
                    packageName = (String) entry.getValue();
                else
                    loadingInfo.put((String) entry.getKey(), (String) entry.getValue());
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return packageName;
    }

    public static Operation getOperationBySymbol(String symb) {
        return operations.get(symb);
    }

    public static String getCellClassName(String key) {
        for(Map.Entry<String, String> entry : cellsMapping.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(key);
            if(matcher.matches())
                return cellsPackageName + "." + entry.getValue();
        }
        return "";
    }

    public static Set<String> getOperations() {
        return operations.keySet();
    }
}
