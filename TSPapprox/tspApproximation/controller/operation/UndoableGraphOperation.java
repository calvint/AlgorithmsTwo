package tspApproximation.controller.operation;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.model.GraphModelInterface;

public interface UndoableGraphOperation extends GraphOperation {
	public void undo(GraphModelInterface gmi);
}
