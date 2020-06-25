package xored.testtask.usova.cell;

//Every cell have a state
// If we try to get value of cell with state IN_PROGRESS => there is cycle in references
public enum CellState {
    UNVISITED,
    VISITED,
    IN_PROCESSING
}
