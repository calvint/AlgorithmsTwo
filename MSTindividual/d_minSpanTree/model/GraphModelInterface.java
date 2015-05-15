package d_minSpanTree.model;

import java.util.ArrayList;

import d_minSpanTree.controller.operation.algorithm.GraphAlgorithm;
import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelObserver;
import d_minSpanTree.model.Vertex;

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
