package tspApproximation.controller.operation;

import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.controller.operation.algorithm.GraphAlgorithm;
import tspApproximation.model.GraphModelInterface;

public class AddGraphAlgorithm implements UndoableGraphOperation {

	private GraphAlgorithm newAlg;

	public AddGraphAlgorithm(GraphAlgorithm alg) {
		newAlg = alg;
	}

	public void execute(GraphModelInterface gmi) {
		gmi.getGraphAlgorithms().add(newAlg);

		gmi.runAlgorithms();
	}

	public void undo(GraphModelInterface gmi) {
		gmi.getGraphAlgorithms().remove(newAlg);
	}

	public String getName() {
		return "Add Graph Algorithm";
	}

}
