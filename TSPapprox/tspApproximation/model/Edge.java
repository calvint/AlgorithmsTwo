package tspApproximation.model;

import tspApproximation.model.Edge;
import tspApproximation.model.EdgeState;
import tspApproximation.model.Vertex;

public class Edge extends Object implements Comparable<Edge>{
	private Vertex start, end;
	private double weight, opacity;
	private EdgeState state;

	public Edge(Vertex v1, Vertex v2) {
		this(v1, v2, Double.NaN, 0.1, EdgeState.UNDIRECTED);
	}

	public Edge(Vertex v1, Vertex v2, double weight, double opacity, EdgeState state) {
		start = v1;
		end = v2;
		this.weight = weight;
		this.opacity = opacity;
		this.state = state;

		v1.getAdjacent().add(v2);
		v2.getAdjacent().add(v1);
	}

	public Vertex getStart() {
		return start;
	}

	public void setStart(Vertex v) {
		start = v;
	}

	public Vertex getEnd() {
		return end;
	}

	public void setEnd(Vertex v) {
		end = v;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double w) {
		weight = w;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double o) {
		opacity = o;
	}

	public EdgeState getState() {
		return state;
	}

	public void setState(EdgeState e) {
		state = e;
	}
	
	public boolean equals(Edge other) {
		if ( this.start.getX() == other.start.getX() & this.start.getY() == other.start.getY() &
				this.end.getX() == other.end.getX() & this.end.getY() == other.end.getY() ) {
			return true;
		} else if ( this.start.getX() == other.end.getX() & this.start.getY() == other.end.getY() &
				this.end.getX() == other.start.getX() & this.end.getY() == other.start.getY() ) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Edge) ) {
			return false;
		} else {
			return equals((Edge) other);
		}
	}

	public String toString() {
		return "["+start.getX() + ","+start.getY()+"],["+end.getX()+","+end.getY()+"]";
	}

	public double getSlope() {
		return (end.getY() - start.getY()) / (end.getX() - start.getX());
	}

	@Override
	public int hashCode() {
		return start.hashCode() + end.hashCode();
	}
	
	@Override
	public int compareTo(Edge o) {
		if (equals(o)) {
			return 0;
		} else {
			double slope1 = getSlope();
			double slope2 = o.getSlope(); 
			if (slope1 < slope2) {
				return -1;
			} else if (slope1 > slope2) {
				return 1;
			} else {
				return 0; //??? why are we here
			}
		}
	}
}
