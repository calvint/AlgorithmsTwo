package tspApproximation.controller.operation;

import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class AddVertex implements UndoableGraphOperation {
	private Vertex add;
	private double posX, posY;

	public AddVertex(double x, double y) {
		posX = x;
		posY = y;
	}

	public void execute(GraphModelInterface gmi) {
		if (add == null)
			add = new Vertex(gmi.getVertices().size() + "", posX, posY);

		gmi.getVertices().add(add);

		gmi.runAlgorithms();
	}

	public void undo(GraphModelInterface gmi) {
		gmi.getVertices().remove(add);
	}

	public String getName() {
		return "Add Vertex";
	}

}
