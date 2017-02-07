package mariam.aco;

import java.util.Observable;
import java.util.Observer;

import mariam.problem.NextReleaseProblem;
import mariam.problem.Problem;
import mariam.sys.Constants;
import mariam.aco.ant.Ant;

public abstract class ACO implements Observer{

	/** Parameter for evaporation */
	public static final double RHO = Constants.RHO;
	
	/** Ants **/
	protected Ant[] ant;

	/** Number of Ants */
	public int numberOfAnts;

	/** Pheromone Matrix **/
	protected double[][] edge;
	
	/** Applied Problem */
	public Problem p;
	
	/** Total of Interations */
	protected int interations;
	
	/** The current interation */
	protected int it = 0;
	
	/** Total of ants that finished your tour */
	protected int finishedAnts = 0;
	
	/** Best Ant in tour */
	public Ant bestAnt;
	
	public ACO(Problem problem, int numberOfAnts, int interations) {
		if (problem == null) {
			throw new IllegalArgumentException("p shouldn't be null");
		}
		if (numberOfAnts <= 0) {
			throw new IllegalArgumentException("numberOfAnts shouldn't be less than 0");
		}
		if (interations <= 0) {
			throw new IllegalArgumentException("interations shouldn't be less than 0");
		}
		
		this.p = problem;
		this.numberOfAnts = numberOfAnts;		
		this.interations = interations;
	}

	public Ant solve() {
		initializeData();
		while (!terminationCondition()) {
			constructAntsSolutions();
            daemonActions(bestAnt); // optional
			updatePheromones();
		}
		return bestAnt;
	}


	private void initializeData() {
		initializePheromones();
		initializeAnts();		
	}

	private void initializePheromones() {
		this.edge = new double[p.getNoOfRequirements()][p.getNoOfRequirements()];

		for (int i = 0; i < p.getNoOfRequirements(); i++) {
			for (int j = 0; j < p.getNoOfRequirements(); j++) {
				this.edge[i][j] = p.getT0();
			}
		}
	}
	
	private boolean terminationCondition() {
		return ++it > interations;
	}

	private void updatePheromones() {
		globalUpdateRule();
	}

	/* Construct Ant solutions */
	private synchronized void constructAntsSolutions() {
		for (int k = 0; k < numberOfAnts; k++) {
			Thread t = new Thread(ant[k],"Ant "+ant[k].id);
			t.start();
		}
		
		//Wait all ants to finish tour
		try{
			wait();
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}	
	}
	
	/**
	 * Call when a ant finishes a tour
	 * 
	 * @Override 
	 */
	public synchronized void update(Observable observable, Object obj) {
		Ant ant = (Ant) obj;

        //ant.explore();

		ant.tourLength = p.evaluate(ant);

		if (p.better(ant, bestAnt)) {
			bestAnt = ant.clone();
		}

		if (++finishedAnts == numberOfAnts) {
			// Continue all execution
			finishedAnts = 0;
			notify();
		}
	}

	public double[][] getEdge() {
		return edge;
	}
	
	public synchronized void setEdge(int j, int i, double value) {
		edge[i][j] = value;
	}
	
	public double getEdge(int i, int j){
		return edge[i][j];
	}

	public abstract void daemonActions(Ant ant);
	
	public abstract void globalUpdateRule();
	
	public abstract void initializeAnts();	
}
