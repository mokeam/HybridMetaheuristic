
package mariam.sys;

import java.util.Date;

import mariam.problem.Problem;
import mariam.aco.ACO;
import mariam.aco.ant.Ant;

public class ExecutionStats {
	
	public double executionTime;
	
	public Problem p;
	
	public ACO aco;
	
	public Ant bestAnt;
	
	public static ExecutionStats execute(ACO aco, Problem p) {
		ExecutionStats ets = new ExecutionStats();
		double startTime = (new Date()).getTime();
		ets.p = p;
		ets.aco = aco;
		ets.bestAnt = aco.solve();
		ets.executionTime = (new Date()).getTime() - startTime;
		return ets;
	}
	
	public void printStats(){
		System.out.println("Problem: "+bestAnt.aco.p.getClass().getSimpleName());
		System.out.println("Instance Name: "+bestAnt.aco.p.getFilename());
		System.out.println("Execution time (ms): "+executionTime);
		System.out.println("Best Solution Found: " + bestAnt.tour);
		System.out.println("Best Solution Weight: " + bestAnt.tour.size());
		//System.out.println("Best Solution Cost: "+ bestAnt.toString());
        System.out.println("Best Solution Cost: "+bestAnt.tourLength );

    }
	
//	public void printDotFormat() {
//		System.out.println(Convert.toDot(bestAnt.aco.getEdge()));
//	}
}
