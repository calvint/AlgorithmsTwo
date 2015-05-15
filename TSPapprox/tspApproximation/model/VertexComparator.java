package tspApproximation.model;

import java.util.Comparator;

import tspApproximation.model.Vertex;

/**
 * 
 * @author Vinushka
 *
 */
public class VertexComparator implements Comparator<Vertex> {

	@Override
	public int compare(Vertex arg0, Vertex arg1) {
		//compare by X, then by Y, then if they're the same
		//return equal. This doesn't serve much purpose
		//other than to sort them for the TreeMap.
		//We choose to sort by x since our screen is bigger in X than in Y
		if (arg0.getX() < arg1.getX()) {
			return -1;
		} else if (arg0.getX() > arg1.getX()) {
			return 1;
		} else {
			if (arg0.getY() < arg1.getY()) {
				return -1;
			} else if (arg0.getY() > arg1.getY()) {
				return 1;
			} else {
				return 0; //same X, same Y, therefore same point.
			}
		}
	}

}
