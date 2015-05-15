package tspApproximation.controller.operation;

import tspApproximation.model.GraphModelInterface;

public interface GraphOperation {

	public void execute(GraphModelInterface gmi);

	public String getName();

}
