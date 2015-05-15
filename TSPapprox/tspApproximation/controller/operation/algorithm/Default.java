package tspApproximation.controller.operation.algorithm;

import tspApproximation.controller.operation.algorithm.GraphAlgorithm;
import tspApproximation.model.Edge;
import tspApproximation.model.GraphModelInterface;

public class Default implements GraphAlgorithm {

	public void execute(GraphModelInterface gmi) {
		for (Edge e : gmi.getEdges()) {
			e.setOpacity(.1);
		}
	}

	public boolean canLiveUpdate() {
		return true;
	}

	public String getName() {
		return "Default";
	}

}
