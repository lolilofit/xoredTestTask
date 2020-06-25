package xored.testtask.usova.cell.implementations;

import xored.testtask.usova.Table;
import xored.testtask.usova.cell.Cell;
import xored.testtask.usova.cell.CellException;
import xored.testtask.usova.cell.CellState;
import xored.testtask.usova.cell.CellsInfo;
import xored.testtask.usova.operations.Operation;
import xored.testtask.usova.operations.OperationException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

//cell with expresion value
public class ExpressionCell extends Cell {
    private String expressionString;

    //cell supports operations with different priorities
    private Stack<Integer> operands = new Stack<>();
    private Stack<Operation> operations = new Stack<>();

    //load all operations
    private List<String> operationsSymbols = new ArrayList<>(CellsInfo.getOperations());

    //save expression in cell
    @Override
    public void init(String str) {
        str = str.substring(1);
        expressionString = str;
    }

    //calculate value of cell with two stacks
    @Override
    public Object getValue(Table table) {
        //if value was calculated before => return value
        if(state.equals(CellState.VISITED))
            return value;

        List<Integer> cur = new ArrayList<>();
        Operation curOperation;

        Integer operandValue;
        int minCur = -1;
        int minPos = -1;
        int prevMinCur = -1;

        //change state to detect cycles in cell references
        super.state = CellState.IN_PROCESSING;

        //find first indexes of operations in expression
        for (String operationsSymbol : operationsSymbols)
            cur.add(expressionString.indexOf(operationsSymbol));

        try {
            //parse expression while there is at least one operation left
            do {
                prevMinCur = minCur;

                //find the smallest index of operation
                minCur = -1;
                for (int k = 0; k < cur.size(); k++) {
                    if (minCur == -1 || minCur > cur.get(k) && cur.get(k) > 0) {
                        minCur = cur.get(k);
                        minPos = k;
                    }
                }

                if (minCur != -1) {
                    //extract operation from the expression
                    curOperation = CellsInfo.getOperationBySymbol(expressionString.substring(minCur, minCur + 1));

                    //parse operand before operation and get it's value
                    operandValue = parseSingleOperand(expressionString.substring(prevMinCur + 1, minCur), table);

                    //push to operands stack
                    operands.push(operandValue);
                    //push operation to operation stack
                    addOperation(curOperation);

                    //shift position to the next occurrence of current operation
                    cur.set(minPos, expressionString.indexOf(operationsSymbols.get(minPos), minCur + 1));
                }
            } while (minCur != -1);

            //parse the last operand
            operands.add(
                    parseSingleOperand(expressionString.substring(prevMinCur + 1), table
                    ));
            freeStack();

            //get cell value
            value = operands.pop();
            if(!operands.isEmpty())
                throw new CellException("WrongSyntax");

        } catch (OperationException | CellException e) {
            //if there was an error during calculation => save error cause
            setError(e.getMessage());
        }

        super.state = CellState.VISITED;
        return value;
    }

    private void freeStack() throws CellException, OperationException {
        //make calculations with the est of operations in stack
        while(!operations.empty()) {
            Integer operationResult = processOperation(operations.pop());
            operands.push(operationResult);
        }
    }

    private Integer getReferenceCellValue(Cell cell, Table table) throws  CellException {
        //detect cyclic references
        if(cell.getState() == CellState.IN_PROCESSING)
            throw new CellException("CycleReference");

        if(cell.isError())
            throw new CellException("ErrorReference");

        Object cellValue = cell.getValue(table);

        //check type of referenced cell value
        if(cellValue instanceof Integer)
            return (Integer) cellValue;

        throw new CellException("WrongType");
    }

    //parse and get value of operand
    private Integer parseSingleOperand(String operand, Table table) throws CellException {
        //try to convert operand to cell reference
        Integer cellCoords = table.convertCellNumber(operand);

        if(cellCoords >= 0) {
            //if there is a cell reference => get it's value
            return getReferenceCellValue(table.getCell(cellCoords), table);
        }
        else {
            //else operand should be represented as integer
            try {
                return Integer.parseInt(operand);
            } catch (NumberFormatException  e) {
                throw new CellException("WrongType");
            }
        }
    }

    //calculate the result of operation
    private Integer processOperation(Operation operation) throws CellException, OperationException {
        LinkedList<Integer> list = new LinkedList<>();
        Integer singleOperand;

        //take n operands to calculate operation
        for(int i = 0; i < operation.getOperandsNum(); i++) {
            singleOperand = operands.pop();

            if(singleOperand == null)
                throw new CellException("NullOperand");
            list.addFirst(singleOperand);
        }

        //calculate operation
        return operation.eval(list.toArray(new Integer[0]));
    }

    //add operation to the stack
    private void addOperation(Operation operation) throws CellException, OperationException {
        if(operations.isEmpty()) {
            operations.push(operation);
            return;
        }

        Integer operationResult;

        //calculate operations that have >= priority than inserting operation
        while(!operations.isEmpty() && operations.peek().getPrior() >= operation.getPrior()) {
            operationResult = processOperation(operations.pop());
            operands.push(operationResult);
        }

        operations.push(operation);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
