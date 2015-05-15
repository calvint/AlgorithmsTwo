package tspApproximation.controller.operation;

import java.util.Random;
import java.util.Stack;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class AddNRandomVertices implements GraphOperation {
    boolean reset;
    int nRandomVertices;
    Random rand = new Random(); // One can seed with a parameter variable here
    static int seed = 1;

	public AddNRandomVertices(boolean reset, int nRandVert, Stack<UndoableGraphOperation> commandStack) {
	    this.reset = reset;
	    nRandomVertices = nRandVert;
        rand.setSeed(seed++);
	}

	public void execute(GraphModelInterface gmi) {
	    // Start from no graph (Without this, it should be just an append of new vertices.)
	    if (reset) {
            gmi.getVertices().clear();
            gmi.getEdges().clear();
	    }

		for (int i=0; i<nRandomVertices; i++) {
            double xPos = rand.nextDouble() * 800; // todo: relate these to actual window space dimensions
            double yPos = rand.nextDouble() * 670; // or, in the very least to GraphViewer.windowWidth and Height
            Vertex v = new Vertex("p"+gmi.getVertices().size(), xPos, yPos);
            
            // Vertex creation O(n)
            gmi.getVertices().add(v);
		}

		gmi.runAlgorithms();
	}

	public String getName() {
		return "Random 100";
	}

}
