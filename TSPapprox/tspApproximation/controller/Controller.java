package tspApproximation.controller;

import tspApproximation.controller.ControllerInterface;
import tspApproximation.controller.operation.AddGraphAlgorithm;
import tspApproximation.controller.operation.AddVertex;
import tspApproximation.controller.operation.FinishMoveVertex;
import tspApproximation.controller.operation.GraphOperationInvoker;
import tspApproximation.controller.operation.LoadGraph;
import tspApproximation.controller.operation.MoveVertex;
import tspApproximation.controller.operation.NewGraph;
import tspApproximation.controller.operation.AddNRandomVertices;
import tspApproximation.controller.operation.RedoOperation;
import tspApproximation.controller.operation.SaveGraph;
import tspApproximation.controller.operation.StartMoveVertex;
import tspApproximation.controller.operation.UndoOperation;
import tspApproximation.controller.operation.algorithm.Default;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.model.Vertex;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Controller implements ControllerInterface {
	private GraphOperationInvoker invoker;

	public Controller(GraphModelInterface model) {
		invoker = new GraphOperationInvoker(model);
		invoker.doOperation(new AddGraphAlgorithm(new Default()));
	}

	public void mousePressedOnVertex(MouseEvent e, Vertex v) {
		invoker.doOperation(new StartMoveVertex(v));
	}

	public void mousePressedOnGraph(MouseEvent e) {
		invoker.doOperation(new AddVertex(e.getX(), e.getY()));
	}

	public void mouseLift(MouseEvent e) {
		if (invoker.getUndoStack().peek() instanceof StartMoveVertex) {
			invoker.doOperation(new FinishMoveVertex());
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (invoker.getUndoStack().peek() instanceof StartMoveVertex) {
			StartMoveVertex smv = (StartMoveVertex) invoker.getUndoStack().peek();
			Vertex v = smv.getTarget();
			invoker.doOperation(new MoveVertex(v, smv.getX() + e.getX(), smv.getY() + e.getY()));
		}
	}

	public void keyPressed(KeyEvent e) {
	}

    public void newGraph() {
        invoker.doOperation(new NewGraph(invoker.getUndoStack()));
    }

    public void fileOpen(String path) {
        invoker.doOperation(new LoadGraph(path, invoker.getUndoStack()));
    }

	public void fileSave(String path) {
		invoker.doOperation(new SaveGraph(path, invoker.getUndoStack()));
	}

	public void undo() {
		invoker.doOperation(new UndoOperation(invoker.getUndoStack(), invoker.getRedoStack()));
	}

	public void redo() {
		invoker.doOperation(new RedoOperation(invoker.getUndoStack(), invoker.getRedoStack()));
	}

    public void randNVertices(boolean reset, int nRandVertices) {
        invoker.doOperation(new AddNRandomVertices(reset, nRandVertices, invoker.getUndoStack()));
    }

	public GraphOperationInvoker getInvoker() {
		return invoker;
	}

}
