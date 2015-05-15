package tspApproximation.model;

import java.util.ArrayList;

import tspApproximation.controller.operation.algorithm.GraphAlgorithm;
import tspApproximation.model.Edge;
import tspApproximation.model.GraphModelObserver;
import tspApproximation.model.Vertex;

public interface GraphModelInterface {
	public ArrayList<Vertex> getVertices();

    public ArrayList<Edge> getEdges();

    public ArrayList<Edge> getDisplayEdges();

	public ArrayList<GraphAlgorithm> getGraphAlgorithms();

	public void runAlgorithms();

	public void addObserver(GraphModelObserver o);

	public void removeObserver(GraphModelObserver o);

	public void notifyObservers();

}
