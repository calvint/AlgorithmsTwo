package tspApproximation.controller.operation.algorithm;

import tspApproximation.controller.operation.algorithm.SortBehavior;
import tspApproximation.model.Edge;

public class AscEdgeWeight implements SortBehavior {

	public boolean compare(Object o1, Object o2) {
		if (o1 instanceof Edge && o2 instanceof Edge) {
			Edge e1 = (Edge) o1;
			Edge e2 = (Edge) o2;
			return e1.getWeight() <= e2.getWeight();
		}
		return false;
	}

}
