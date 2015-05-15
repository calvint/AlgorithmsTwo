package tspApproximation.controller.operation.algorithm;
import java.util.Comparator;

import tspApproximation.model.Edge;

public class EdgeWeightComparator implements Comparator<Edge> {

	@Override
	public int compare(Edge arg0, Edge arg1) {
		double weight0 = arg0.getWeight();
		double weight1 = arg1.getWeight();
		if (weight0 < weight1) {
			return -1;
		} else if (weight0 > weight1) {
			return 1;
		} else {
			return 0;
		}
	}
	
}
