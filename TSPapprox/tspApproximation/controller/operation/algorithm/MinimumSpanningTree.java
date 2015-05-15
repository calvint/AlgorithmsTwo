package tspApproximation.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import tspApproximation.controller.operation.algorithm.AscEdgeWeight;
import tspApproximation.controller.operation.algorithm.GraphAlgorithm;
import tspApproximation.controller.operation.algorithm.QuickSort;
import tspApproximation.model.Edge;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class MinimumSpanningTree implements GraphAlgorithm {
	private ArrayList<ArrayList<Vertex>> forest;
	private HashMap<Vertex,ArrayList<Vertex>> vertexToTree;

	@Override
    public void execute(GraphModelInterface gmi) {
		forest = new ArrayList<ArrayList<Vertex>>();
//		for (Edge e : gmi.getEdges()) {
//			e.setOpacity(.05);
//		}
		ArrayList<Edge> finalTree = new ArrayList<Edge>();
		vertexToTree = new HashMap<>();
	//	System.out.println("Edges (after edge creation): " + gmi.getEdges().size());
		ArrayList<Object> edges = new ArrayList<Object>();
		edges.addAll(gmi.getEdges());

		for (Vertex v : gmi.getVertices()) {
			ArrayList<Vertex> tree = new ArrayList<Vertex>();
			tree.add(v);
			vertexToTree.put(v,tree);
			forest.add(tree);
		}

//		for (Edge e : gmi.getEdges()) {
//		    e.setOpacity(0.1);
//		}

        // Kruskal's algorithm O(f(nE,nV)???) in an efficient time complexity implementation.
        long startTime = System.nanoTime(); // Start the total timing
        // Quicksort of the entire edge array puts us at O(???) time complexity

		(new QuickSort(edges, new AscEdgeWeight())).sort();

		for (int i = 0; i < edges.size(); i++) {
			Edge e = (Edge) edges.get(i);
			Vertex start = e.getStart();
			Vertex end = e.getEnd();
			ArrayList<Vertex> tree1 = findTreeWithMap(start);
			ArrayList<Vertex> tree2 = findTreeWithMap(end);
			if (tree1 != tree2) {
				finalTree.add(e);
				tree1.addAll(tree2);
				//now update the tree holder.
				for (Vertex vert : tree2) {
					//vertexToTree.remove(vert);
					vertexToTree.put(vert,tree1); //overwrites
				}
				forest.remove(tree2);
			}
		}
        long endTime  = System.nanoTime(); // Finish the total timing
        float timeElapsed = (endTime-startTime)/1000000.0f; // milliseconds
        System.out.println("MST (Kruskal) time O(f(nE,nV)???):" + timeElapsed);

		for (Edge e : finalTree) {
			e.setOpacity(1);
		}

		gmi.getDisplayEdges().clear();
        //gmi.getDisplayEdges().addAll(finalTree); // Only adding the MST edges for display
        gmi.getDisplayEdges().addAll(gmi.getEdges()); // Adding all edges for display (Use this only if Delaunay)
	}

	private ArrayList<Vertex> findTreeWithMap(Vertex vert) {
		return vertexToTree.get(vert);
	}

//Old findTree, for debugging and benchmarking purposes.
	private ArrayList<Vertex> findTree(Vertex vert) {
		for (ArrayList<Vertex> tree : forest) {
			for (Vertex v : tree)
				if (vert == v)
					return tree;
		}
		return null;
	}

	@Override
    public String getName() {
		return "Minimum Spanning Tree";
	}

	@Override
    public boolean canLiveUpdate() {
		return true;
	}

}
