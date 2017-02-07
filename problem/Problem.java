package mariam.problem;

import mariam.sys.InstanceReader;
import mariam.aco.ant.Ant;

public abstract class Problem {
	
	protected InstanceReader reader;
	
	private String filename;
	
	public Problem(String filename){
		this.filename = filename;
		this.reader = new InstanceReader(filename);
	}
	
	public String getFilename() {
		return filename;
	}

	/**
	 * Get heuristic information
	 * 
	 * @param i Node i
	 * @param j Node j
	 * @return heuristic information between node i and j
	 */
	public abstract double getNij(int i, int j);
	
	/**
	 * Return number of nodes
	 * @return number of nodes
	 */
	public abstract int getNoOfRequirements();


	public abstract void setWeightCost(Ant ant);
	
	/**
	 * Return the initial pheromone
	 * @return Initial pheromone
	 */
	public abstract double getT0();

	public abstract void initializeTheMandatoryNeighborhood(Ant ant);
	
	public abstract void updateTheMandatoryNeighborhood(Ant ant);

	public abstract double evaluate(Ant ant);	
	
	public abstract boolean better(Ant ant, Ant bestAnt);

	public abstract double getDeltaTau(Ant ant,int i, int j);
}
