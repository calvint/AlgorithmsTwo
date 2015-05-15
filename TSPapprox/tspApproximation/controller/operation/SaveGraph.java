package tspApproximation.controller.operation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

import tspApproximation.controller.operation.GraphOperation;
import tspApproximation.controller.operation.UndoableGraphOperation;
import tspApproximation.model.Edge;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;

public class SaveGraph implements GraphOperation {
	private File file;
	public SaveGraph(String path, Stack<UndoableGraphOperation> commandStack) {
		file = new File(path);
	}

	public void execute(GraphModelInterface gmi) {
		BufferedWriter bf = null;
		try {
			bf = new BufferedWriter(new FileWriter(file));

            for ( Vertex v : gmi.getVertices() ) {
                bf.write(v.getName()+" "+v.getX()+" "+v.getY()+"\r\n");
            }
            bf.write("----------\r\n");

            for ( Edge e : gmi.getEdges() ) {
                bf.write(e.getStart().getName()+" "+e.getEnd().getName()+" "+e.getWeight()+"\r\n");
            }

            bf.close();
		} catch (Exception io) {
			io.printStackTrace();
			if (bf != null)
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		gmi.runAlgorithms();
	}

	public String getName() {
		return "Save";
	}

}
