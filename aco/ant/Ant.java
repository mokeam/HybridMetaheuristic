package mariam.aco.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import mariam.sys.Constants;
import mariam.util.PseudoRandom;
import mariam.aco.ACO;

public abstract class Ant extends Observable implements Runnable {
	
	public static int ANT_ID = 1;
	
	/** Importance of trail */
	public static final double ALPHA = Constants.ALPHA;
	
	//** Importance of heuristic evaluate */
	public static final double BETA = Constants.BETA;

	/** Identifier */
	public int id = ANT_ID++;
	
	public ACO aco;
	
	public List<Integer> tour;
	
	/** The Current Node */
	public int currentNode;
	
	public int[][] path;
	
	public List<Integer> nodesToVisit;

	public double tourLength;
	
	public Ant(ACO aco) {
		this.aco = aco;
		reset();		
	}
	
	public void reset(){
		this.currentNode = -1;
		this.tourLength = 0;
		this.nodesToVisit = new ArrayList<Integer>();
		this.tour = new ArrayList<Integer>();
		this.path = new int[aco.p.getNoOfRequirements()][aco.p.getNoOfRequirements()];
	}

	@Override
	public void run() {
		init();
		explore();		
		setChanged();
		notifyObservers(this);
	}
	
	public void init(){
		reset();
		this.currentNode = PseudoRandom.randInt(0, aco.p.getNoOfRequirements() - 1);
		this.tour.add(new Integer(currentNode));
		this.aco.p.initializeTheMandatoryNeighborhood(this);
	}
	
	@Override
	public String toString() {
		return "Ant " + id + " " + tour+" "+tourLength;
	}

	/**
	 * Construct the solutions
	 */
	public abstract void explore();

	/**
	 * Clone the ant
	 */
	public abstract Ant clone();
}
