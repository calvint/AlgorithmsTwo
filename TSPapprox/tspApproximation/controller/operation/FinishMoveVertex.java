package tspApproximation.controller.operation;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.model.GraphModelInterface;

public class FinishMoveVertex implements GraphOperation {

	public void execute(GraphModelInterface gmi) {
		gmi.runAlgorithms();
	}

	public String getName() {
		return "Move Vertex";
	}

}
