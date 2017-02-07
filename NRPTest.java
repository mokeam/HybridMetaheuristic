package mariam;
import mariam.aco.ACO;
import mariam.aco.AntColonySystem;
import mariam.problem.NextReleaseProblem;
import mariam.problem.Problem;
import mariam.sys.ExecutionStats;

public class NRPTest {

	private static int ants = 10;

	private static int interations = 10;

	private static NextReleaseProblem pn  = new NextReleaseProblem("/home/mokeam/AndroidStudioProjects/SBSE/app/src/main/java/mariam/in/data3.nrp");

	public static void main(String[] args) {
		Problem p = pn;


//		ACO aco = new AntSystem(p, ants, interations);
		ACO aco = new AntColonySystem(p, ants, interations);

		ExecutionStats es = ExecutionStats.execute(aco, p);

		es.printStats();



	}
}
