package tspApproximation.controller.operation;

import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class StartMoveVertex implements UndoableGraphOperation {

	private Vertex target;
	private double origXPos, origYPos;

	public StartMoveVertex(Vertex v) {
		target = v;
	}

	public void execute(GraphModelInterface gmi) {
		origXPos = target.getX();
		origYPos = target.getY();
	}

	public void undo(GraphModelInterface gmi) {
		target.setX(origXPos);
		target.setY(origYPos);
	}

	public Vertex getTarget() {
		return target;
	}

	public double getX() {
		return origXPos;
	}

	public double getY() {
		return origYPos;
	}

	public String getName() {
		return "Move Vertex";
	}

}
