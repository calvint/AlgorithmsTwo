package tspApproximation.controller.operation;

import java.util.Stack;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.model.GraphModelInterface;

public class GraphOperationInvoker {
	private GraphModelInterface graphModel;
	private Stack<UndoableGraphOperation> undoStack = new Stack<UndoableGraphOperation>();
	private Stack<UndoableGraphOperation> redoStack = new Stack<UndoableGraphOperation>();

	public GraphOperationInvoker(GraphModelInterface gmi) {
		graphModel = gmi;
	}

	public void doOperation(GraphOperation op) {
		if (op instanceof UndoableGraphOperation) {
			undoStack.push((UndoableGraphOperation) op);
			redoStack.clear();
		}

		op.execute(graphModel);

		graphModel.notifyObservers();
	}

	public Stack<UndoableGraphOperation> getUndoStack() {
		return undoStack;
	}

	public Stack<UndoableGraphOperation> getRedoStack() {
		return redoStack;
	}

}
