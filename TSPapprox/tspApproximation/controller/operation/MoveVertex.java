package tspApproximation.controller.operation;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class MoveVertex implements GraphOperation {
	private Vertex target;
	private double xMoveTo, yMoveTo;

	public MoveVertex(Vertex v, double x, double y) {
		target = v;
		xMoveTo = x;
		yMoveTo = y;
	}

	public void execute(GraphModelInterface gmi) {
		target.setX(xMoveTo);
		target.setY(yMoveTo);
		gmi.runAlgorithms();
	}

	public String getName() {
		return "Move Vertex";
	}

}
