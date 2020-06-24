package xored.testtask.usova.cell.implementations;

import xored.testtask.usova.Table;
import xored.testtask.usova.cell.Cell;
import xored.testtask.usova.cell.CellState;
import xored.testtask.usova.cell.CellsInfo;
import xored.testtask.usova.operations.Operation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


//rewrite!
public class ExpressionCell extends Cell {
    //expression consists of operands as String and Operations
    private LinkedList<Object> expression = new LinkedList<>();

    @Override
    public Object getValue(Table table) {

        if(state.equals(CellState.VISITED))
            return value;

        Stack<Object> stack = new Stack<>();

        Integer firstOperand;
        Object operationResult;

        super.state = CellState.IN_PROCESSING;

        for (Object expressionPart : expression) {
            if (expressionPart instanceof Operation) {
                operationResult = processOperation(stack, (Operation) expressionPart, table);

                if (operationResult == null)
                    break;

                stack.push(operationResult);
            } else {
                firstOperand = parseSingleOperand(expressionPart, table);

                if (firstOperand == null)
                    break;

                stack.push(firstOperand);
            }
        }

        super.state = CellState.VISITED;

        if(!error) {
            value = stack.pop();
            return value;
        }
        return null;
    }


    private Integer getReferenceCellValue(Cell cell, Table table) {
        if(cell.getState() == CellState.IN_PROCESSING) {
            setError("CycleReference");
            return null;
        }
        if(cell.isError()) {
            setError("ErrorReference");
            return null;
        }

        Object cellValue = cell.getValue(table);
        if(cellValue instanceof Integer)
            return (Integer) cellValue;
        return null;
    }

    private Integer parseSingleOperand(Object objectOperand, Table table) {
        if(objectOperand instanceof Integer)
            return (Integer) objectOperand;

        if(!(objectOperand instanceof  String)) {
            setError("WrongSyntax");
            return null;
        }

        String operand = (String) objectOperand;
        Integer cellCoords = table.convertCellNumber(operand);

        if(cellCoords >= 0) {
            return getReferenceCellValue(table.getCell(cellCoords), table);
        }
        else {
            try {
                return Integer.parseInt(operand);
            } catch (NumberFormatException  e) {
                setError("ErrorCellType");
                return null;
            }
        }
    }

    private Object processOperation(Stack<Object> stack, Operation operation, Table table) {
        LinkedList<Integer> operands = new LinkedList<>();
        Object objectOperand;
        Integer singleOperand;

        for(int i = 0; i < operation.getOperandsNum(); i++) {
            objectOperand = stack.pop();
            singleOperand = parseSingleOperand(objectOperand, table);

            if(singleOperand == null)
                return null;
            operands.addFirst(singleOperand);
        }

        objectOperand = operation.eval(operands.toArray(new Integer[0]));
        if(objectOperand == null) {
            setError("EvaluationError");
            return null;
        }
        return objectOperand;
    }

    private void addOperation(Stack<Operation> stack, Operation operation) {
        if(stack.isEmpty()) {
            stack.push(operation);
            return;
        }

        while(stack.peek().getPrior() >= operation.getPrior())
            expression.add(stack.pop());

        stack.push(operation);
    }

    private void freeOperationStack(Stack<Operation> stack) {
        while(!stack.isEmpty())
            expression.add(stack.pop());
    }

    @Override
    public void init(String str) {
        str = str.substring(1);
        if(str.length() == 0)
            return;

        List<String> operationsSymbols = new ArrayList<>(CellsInfo.getOperations());
        List<Integer> cur = new ArrayList<>();

        Stack<Operation> stack = new Stack<>();
        Operation curOperation;

        int minCur = -1;
        int minPos = -1;
        int prevMinCur = -1;

        for (String operationsSymbol : operationsSymbols)
            cur.add(str.indexOf(operationsSymbol));

        do {
            prevMinCur = minCur;

            minCur = -1;
            for(int k = 0; k < cur.size(); k++) {
                if(minCur == -1 || minCur > cur.get(k) && cur.get(k) > 0) {
                    minCur = cur.get(k);
                    minPos = k;
                }
            }

            if(minCur != -1) {
                curOperation = CellsInfo.getOperationBySymbol(str.substring(minCur, minCur + 1));
                expression.add(str.substring(prevMinCur + 1, minCur));
                addOperation(stack, curOperation);

                cur.set(minPos, str.indexOf(operationsSymbols.get(minPos), minCur + 1));
            }
        } while (minCur != -1);

        expression.add(str.substring(prevMinCur + 1));

        freeOperationStack(stack);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
