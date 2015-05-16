package tspApproximation.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import tspApproximation.model.Edge;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class TSPApproximation implements GraphAlgorithm {
	ArrayList<Edge> tspApprox = new ArrayList<Edge>();
	

	@Override
	public void execute(GraphModelInterface gmi) {
		tspApprox = new ArrayList<Edge>();
		if (gmi.getEdges().size() >= 2) {
			Vertex lastToBeAdded = tspApproxRecursive(gmi.getEdges().get(0).getStart(), gmi.getEdges().get(0).getStart(), gmi.getEdges().get(0).getStart(), gmi.getEdges());
			tspApprox.add(new Edge(gmi.getEdges().get(0).getStart(), lastToBeAdded));
			
			gmi.getDisplayEdges().clear();
	        gmi.getDisplayEdges().addAll(tspApprox);
		}
	}

	private Vertex tspApproxRecursive(Vertex currentVertex, Vertex parentVertex, Vertex lastAdded, ArrayList<Edge> minimumSpanningTree) {
		Edge pilotEdge;
		//if this is not the top itteration
		if (!currentVertex.equals(lastAdded)) {
			pilotEdge = new Edge(currentVertex, parentVertex);
			Edge approxEdge = new Edge(currentVertex, lastAdded);
			tspApprox.add(approxEdge);
			lastAdded = currentVertex;
		} else {
			pilotEdge = new Edge(currentVertex, new Vertex("dummyVertex", currentVertex.getX(), currentVertex.getY() + 1));
		}
		//find all of the edges that include the current vertex
		ArrayList<Edge> connectedEdges = new ArrayList<Edge>();
		for (Edge edge : minimumSpanningTree) {
			if (edge.getStart().equals(currentVertex) || edge.getEnd().equals(currentVertex)) {
				if (!edge.equals(pilotEdge)) {
					connectedEdges.add(edge);
				}
			}
		}
		if (connectedEdges.size() != 0) {
			//sort the edges in clockwise fashion and store endpoints
			HashMap<Double, Vertex> nextVertexMap = new HashMap<Double, Vertex>();
			for (Edge connectedEdge : connectedEdges) {
				Double angle = connectedEdge.getClockwiseAngleFromEdge(pilotEdge, currentVertex);
				nextVertexMap.put(angle, connectedEdge.getOtherVertex(currentVertex));
			}
			SortedSet<Double> keys = new TreeSet<Double>(nextVertexMap.keySet());
			//loop over endpoints in clockwise fashion and recurse
			for (Double key : keys) {
				lastAdded = tspApproxRecursive(nextVertexMap.get(key),currentVertex, lastAdded, minimumSpanningTree);
			}
		}
		if (parentVertex == currentVertex) {
			tspApprox.add(new Edge(currentVertex, lastAdded));
		}
		return lastAdded;
	}


	@Override
	public String getName() {
		return "Traveling Salesman Problem Approximation";
	}

	@Override
	public boolean canLiveUpdate() {
		return true;
	}

}
