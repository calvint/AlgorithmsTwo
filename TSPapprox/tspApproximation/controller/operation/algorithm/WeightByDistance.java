package tspApproximation.controller.operation.algorithm;

import tspApproximation.controller.operation.algorithm.GraphAlgorithm;
import tspApproximation.model.Edge;
import tspApproximation.model.GraphModelInterface;

public class WeightByDistance implements GraphAlgorithm {

	public void execute(GraphModelInterface gmi) {
		for (Edge e : gmi.getEdges())
			e.setWeight(distance(e));
	}

	public String getName() {
		return "Weight Edges by Distance";
	}

	public boolean canLiveUpdate() {
		return true;
	}

	public double distance(Edge e) {
		double x = e.getStart().getX() - e.getEnd().getX();
		double y = e.getStart().getY() - e.getEnd().getY();
		return Math.sqrt(x * x + y * y);
	}

}
