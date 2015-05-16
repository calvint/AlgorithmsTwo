package tspApproximation;

import javafx.application.Application;
import javafx.stage.Stage;
import tspApproximation.controller.Controller;
import tspApproximation.controller.ControllerInterface;
import tspApproximation.controller.operation.AddGraphAlgorithm;
//import tspApproximation.controller.operation.algorithm.ConnectAllVertices;
import tspApproximation.controller.operation.algorithm.DelaunayTriangulation;
import tspApproximation.controller.operation.algorithm.MinimumSpanningTree;
import tspApproximation.controller.operation.algorithm.WeightByDistance;
import tspApproximation.controller.operation.algorithm.TSPApproximation;
//import tspApproximation.controller.operation.algorithm.WeightByDistance;
import tspApproximation.model.GraphModel;
import tspApproximation.model.GraphModelInterface;
import tspApproximation.view.GraphViewer;
import tspApproximation.view.ViewerInterface;

public class UltimateGraph extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		GraphModelInterface model = new GraphModel();
		ControllerInterface controller = new Controller(model);
		ViewerInterface view = new GraphViewer(model, controller, stage);
		view.init();

		//controller.getInvoker().doOperation(new AddGraphAlgorithm(new ConnectAllVertices()));
        controller.getInvoker().doOperation(new AddGraphAlgorithm(new DelaunayTriangulation()));
        controller.getInvoker().doOperation(new AddGraphAlgorithm(new WeightByDistance()));
		controller.getInvoker().doOperation(new AddGraphAlgorithm(new MinimumSpanningTree()));
		controller.getInvoker().doOperation(new AddGraphAlgorithm(new TSPApproximation()));
	}
}
