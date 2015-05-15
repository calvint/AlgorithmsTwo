package tspApproximation.controller.operation;

import java.util.Stack;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.model.GraphModelInterface;

public class NewGraph implements GraphOperation {
	private Stack<UndoableGraphOperation> undoStack;

	public NewGraph(Stack<UndoableGraphOperation> commandStack) {
		undoStack = commandStack;
	}

	public void execute(GraphModelInterface gmi) {
		undoStack.clear();
		gmi.getVertices().clear();
		gmi.getEdges().clear();

		gmi.runAlgorithms();
	}

	public String getName() {
		return "NewGraph";
	}

}
