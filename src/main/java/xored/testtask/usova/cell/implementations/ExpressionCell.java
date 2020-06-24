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

public class ExpressionCell extends Cell {
    //expression consists of operands as String and Operations
    private LinkedList<Object> expression = new LinkedList<>();

    private Integer evalStringOperand(String operand, Table table) {
        Integer cellCoords = table.convertCellNumber(operand);

        if(cellCoords >= 0) {
            Cell cell = table.getCell(cellCoords);

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
        else {
            try {
                return Integer.parseInt(operand);
            } catch (NumberFormatException  e) {
                setError("ErrorCellType");
                return null;
            }
        }
    }

    private Integer processOperand(Object listObject, Table table) {
        if(listObject instanceof Integer)
            return (Integer) listObject;

        if(!(listObject instanceof  String)) {
            setError("WrongSyntax");
            return null;
        }
        return evalStringOperand((String)listObject, table);
    }

    @Override
    public Object getValue(Table table) {

        if(state.equals(CellState.VISITED))
            return value;

        Stack<Object> stack = new Stack<>();

        Integer firstOperand;
        Integer secondOperand;
        Object expressionPart;
        Object objectOperand;

        super.state = CellState.IN_PROCESSING;

        for (int i = 0; i < expression.size(); i++) {
            expressionPart = expression.get(i);

            //refactor1!!!
            if(expressionPart instanceof Operation) {
                objectOperand = stack.pop();
                secondOperand = processOperand(objectOperand, table);
                if(secondOperand == null)
                    break;

                objectOperand = stack.pop();
                firstOperand = processOperand(objectOperand, table);
                if(firstOperand == null)
                    break;

                objectOperand = ((Operation) expressionPart).eval(firstOperand, secondOperand);
                if(objectOperand == null) {
                    setError("EvaluationError");
                    break;
                }

                stack.push(objectOperand);
            }
            else {
                firstOperand = processOperand(expressionPart, table);
                if(firstOperand == null)
                    break;
                stack.push(firstOperand);
            }
        }

        super.state = CellState.VISITED;

        value = stack.pop();
        return value;
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
        while(!stack.isEmpty()) {
            expression.add(stack.pop());
        }
    }

    @Override
    public void init(String str) {
        str = str.substring(1);
        if(str.length() == 0)
            return;

        Stack<Operation> stack = new Stack<>();
        Operation curOperation;

        int minCur = -1;
        int minPos = -1;
        int prevMinCur = -1;

        List<String> operationsSymbols = new ArrayList<>(CellsInfo.getOperations());
        List<Integer> cur = new ArrayList<>();

        for(int i = 0; i < operationsSymbols.size(); i ++)
            cur.add(str.indexOf(operationsSymbols.get(i)));

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
