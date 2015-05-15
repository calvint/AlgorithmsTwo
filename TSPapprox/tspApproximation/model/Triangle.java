package tspApproximation.model;

import java.util.ArrayList;

import tspApproximation.model.Edge;
import tspApproximation.model.Vertex;

public class Triangle {

	ArrayList<Edge> edgeArray = new ArrayList<Edge>();

	public Triangle(Vertex vertex, Vertex vertex2, Vertex vertex3) {
		edgeArray.add(new Edge(vertex, vertex2));
		edgeArray.add(new Edge(vertex, vertex3));
		edgeArray.add(new Edge(vertex3, vertex2));
	}

	public Triangle(Edge edge, Vertex vertex3) {
		Vertex vertex = edge.getStart();
		Vertex vertex2 = edge.getEnd();
		edgeArray.add(new Edge(vertex, vertex2));
		edgeArray.add(new Edge(vertex, vertex3));
		edgeArray.add(new Edge(vertex3, vertex2));
	}

	public boolean pointInsideCircumcircle(Vertex v) {
	    // Get the first three vertices
	    Vertex v1 = edgeArray.get(0).getStart();
	    Vertex v2 = edgeArray.get(2).getEnd();
	    Vertex v3 = edgeArray.get(2).getStart();
	    // Ensure they're in counter-clockwise order
	    if (!isCounterClockwise(v1, v2, v3)) {
	        // If they're not, swap the last two points
	        v3 = edgeArray.get(2).getEnd();
	        v2 = edgeArray.get(2).getStart();
	    }

	    // The reason for saving so many points is to reduce the number of calculations.
	    final double Ax = v1.getX();
	    final double Ay = v1.getY();
	    final double Bx = v2.getX();
        final double By = v2.getY();
	    final double Cx = v3.getX();
	    final double Cy = v3.getY();
	    final double Dx = v.getX();
        final double Dy = v.getY();
        final double DDx = Dx*Dx;
        final double DDy = Dy*Dy;
	    final double a = (Ax - Dx);
	    final double b = (Ay - Dy);
	    final double c = (Ax*Ax - DDx) + (Ay*Ay - DDy);
	    final double d = (Bx - Dx);
	    final double e = (By - Dy);
	    final double f = (Bx*Bx - DDx) + (By*By - DDy);
	    final double g = (Cx - Dx);
	    final double h = (Cy - Dy);
	    final double i = (Cx*Cx - DDx) + (Cy*Cy - DDy);

	    // These three lines should be equivalent
//	    return  a*e*i + b*f*g + c*d*h - c*e*g - b*d*i - a*f*h;
//	    return (a*e*i + b*f*g + c*d*h) - (c*e*g + b*d*i + a*f*h) > 0;
		return (a*e*i + b*f*g + c*d*h) > (c*e*g + b*d*i + a*f*h);
	}

    /** Taken from convex hull code */
    public static boolean isCounterClockwise(Vertex v1, Vertex v2, Vertex v3) {
        double value = 0;
        value += (v2.getX() - v1.getX()) * (v3.getY() - v1.getY());
        value -= (v2.getY() - v1.getY()) * (v3.getX() - v1.getX());
        return value > 0;
    }

	public ArrayList<Edge> getEdges() {
		return edgeArray;
	}

	@Override
    public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Triangle {\n");
	    final Vertex v1 = edgeArray.get(0).getStart();
        final Vertex v2 = edgeArray.get(2).getStart();
        final Vertex v3 = edgeArray.get(2).getEnd();
	    sb.append("(" + v1.getX() + ", " + v1.getY() + ")\n");
	    sb.append("(" + v2.getX() + ", " + v2.getY() + ")\n");
	    sb.append("(" + v3.getX() + ", " + v3.getY() + ")}");
	    return sb.toString();
	}
}
