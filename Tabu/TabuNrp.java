package mariam.Tabu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import mariam.aco.ACO;
import mariam.aco.ant.Ant;
import mariam.problem.Problem;

/**
 * Created by mokeam on 1/17/17.
 */
public class TabuNrp extends Problem {

    protected int requirements;

    protected int customers;

    protected int[] cost;

    protected double[] satisfaction;

    public int[][] customerSatisfaction;

    protected int[] customerImportance;

    protected double satR;

    protected List<Integer> selectedReq;

    protected double B;

    public double executionTime;

    public double startTime;

    private List<Solution> solutions;

    private  Solution returnValue;

    public TabuNrp(String filename) {
        super(filename);

        reader.open();

        int[] param = reader.readIntVector(" ");
        this.requirements = param[0];
        this.customers = param[1];
        this.customerImportance = reader.readIntVector(" ");
        this.cost = reader.readIntVector(" ");
        this.customerSatisfaction = reader.readIntMatrix(customers,requirements," ");
        this.satisfaction = new double[requirements];
        this.B = reader.readInt();
        this.selectedReq = new ArrayList<>();

        reader.close();

        this.startTime = (new Date()).getTime();
    }

    public void buildInstance() {
        Integer instanceSize = 10;

        solutions = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < instanceSize; i++) {
            Integer currentValue = cost[i];

            SomeSolution solution = new SomeSolution();
            solution.neighbors = new LinkedList<>();
            solution.value = currentValue;

            solutions.add(solution);
        }

        Collections.shuffle(solutions);

        for (int i = 0; i < instanceSize; i++) {
            Solution solution = solutions.get(i);

            Integer neighborsCount = random.nextInt(7) + 3;

            for (int j = 0; j < neighborsCount; j++) {
                Solution randomNeighbor = solutions.get(random.nextInt(instanceSize));
                solution.getNeighbors().add(randomNeighbor);
            }
        }

        Collections.sort(solutions, new Comparator<Solution>() {
            @Override
            public int compare(Solution a, Solution b) {
                return a.getValue().compareTo(b.getValue());
            }
        });
    }

    public double getBound(){
        return (0.7 * getTotalCost());
    }
    public int getTotalCost(){
        int sum = 0;
        for(int i = 0; i < cost.length; i++){
            sum += cost[i];
        }
        return sum;
    }
    public void runAlgorithm() {

        int total = 0;
        int returnedValueIndex = 0;
        int tabuList = 100;
        for (int s = 5, j = 0; s < tabuList; s++,j++) { //the size of the tabu list

            Solution initialSolution = solutions.get(new Random().nextInt(solutions.size()));

            for (int i = 1; i <= 1; i ++) { //the amount of iterations (50%, 100%, 150% and 200%)
                Integer maxIterations = i;

                //System.out.println(String.format("Running TS with tabu list size [%s] and [%s] iterations. Instance size [%s]", s, maxIterations, solutions.size()));

                TabuSearch ts = setupTS(s, maxIterations);
                Solution returnValue = ts.run(initialSolution);

                returnedValueIndex = solutions.indexOf(returnValue);

               // System.out.println(solutions.get(returnedValueIndex).getValue());


                //System.out.println(String.format(" [%s]", returnedValueIndex));
            }
            try {
                selectedReq.get( j );
            } catch ( IndexOutOfBoundsException e ) {
                selectedReq.add( j, solutions.get(returnedValueIndex).getValue() );
            }
            //selectedReq.add(solutions.get(returnedValueIndex).getValue());


        }
        double weight = 0;
        List <Integer> selected = new ArrayList<>();
        for(int k = 0; k < selectedReq.size();k++){
            if(selectedReq.get(k) <= getBound()) {
                total += selectedReq.get(k);
                if(total <= getBound() && !selected.contains(selectedReq.get(k))){
                    weight += selectedReq.get(k);
                    selected.add(selectedReq.get(k));
                    System.out.println(selectedReq.get(k));
                }
            }
        }
        System.out.println("Requirement Weight:" +selected.size());
        System.out.println("Requirement Cost:" +weight);
        this.executionTime = (new Date()).getTime() - this.startTime;
    }

    public TabuSearch setupTS(Integer tabuListSize, Integer iterations) {
        return new TabuSearch(new StaticTabuList(tabuListSize), new IterationsStopCondition(iterations), new BasicNeighborSolutionLocator());
    }

    private static class SomeSolution implements Solution {

        Integer value;
        List<Solution> neighbors;

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public List<Solution> getNeighbors() {
            return neighbors;
        }


        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SomeSolution other = (SomeSolution) obj;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }
    public static void main(String[] Args){

        TabuNrp bs = new TabuNrp("/home/mokeam/AndroidStudioProjects/SBSE/app/src/main/java/mariam/in/data2.nrp");
        bs.buildInstance();
        bs.runAlgorithm();
        System.out.println("Execution time (ms): "+bs.executionTime);

    }
    @Override
    public double getNij(int i, int j) {
        return 0;
    }

    @Override
    public int getNoOfRequirements() {
        return 0;
    }

    @Override
    public void setWeightCost(Ant ant) {

    }

    @Override
    public double getT0() {
        return 0;
    }

    @Override
    public void initializeTheMandatoryNeighborhood(Ant ant) {

    }

    @Override
    public void updateTheMandatoryNeighborhood(Ant ant) {

    }

    @Override
    public double evaluate(Ant ant) {
        return 0;
    }

    @Override
    public boolean better(Ant ant, Ant bestAnt) {
        return false;
    }

    @Override
    public double getDeltaTau(Ant ant, int i, int j) {
        return 0;
    }
}
