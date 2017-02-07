package mariam.problem;

import java.util.ArrayList;
import java.util.List;

import mariam.aco.ant.Ant;

public class NextReleaseProblem extends Problem{
	
	protected int requirements;
	
	protected int customers;
	
	protected int[] cost;
	
	protected double[] satisfaction;
	
	public int[][] customerSatisfaction;
	
	protected int[] customerImportance;
	
	protected double satR;

    public double weightCost;

	
	protected double B;
	
	protected static double MI = 1.0;

	public NextReleaseProblem(String filename) {
		super(filename);
		
		reader.open();
		
		int[] param = reader.readIntVector(" ");
		this.requirements = param[0];
		this.customers = param[1];
		this.customerImportance = reader.readIntVector(" ");
		this.cost = reader.readIntVector(" ");
		this.customerSatisfaction = reader.readIntMatrix(customers,requirements," ");
		this.satisfaction = new double[requirements];
		//this.B = reader.readInt();
		
		reader.close();
        weightCost = 0;
		
		for (int i = 0; i < requirements; i++) {
			for (int j = 0; j < customers; j++) {
				satisfaction[i] += customerImportance[j] * customerSatisfaction[j][i];
			}
		}
		
		for (int i = 0; i < requirements; i++) {
			satR += satisfaction[i];
		}
	}




    public double getBound(){
        return (0.3 * getTotalCost());
    }


	@Override
	public double getNij(int i, int j) {
		//return MI * (satisfaction[j] / cost[j]);
        return MI * ( cost[i] / satisfaction[j] );
	}

	@Override
	public int getNoOfRequirements() {
		return requirements;
	}

	@Override
	public double getT0() {
		return 1 / satR;
	}

	@Override
	public void initializeTheMandatoryNeighborhood(Ant ant) {
		for (int i = 0; i < getNoOfRequirements(); i++) {
			if(i != ant.currentNode){
				if (cost[i] <= getBound()) {
					ant.nodesToVisit.add(new Integer(i));

				}
			}
		}		
	}

	public int getTotalCost(){
		int sum = 0;
		for(int i = 0; i < cost.length; i++){
			sum += cost[i];
		}
		return sum;
	}
	@Override
	public void updateTheMandatoryNeighborhood(Ant ant) {
		List<Integer> nodesToRemove = new ArrayList<Integer>();

		double totalCost = 0.0;

		for (Integer i : ant.tour) {
			totalCost += cost[i];
		}

		for (Integer i : ant.nodesToVisit) {
			if (totalCost + cost[i] > getBound()) {
				nodesToRemove.add(i);
			}

		}

		for (Integer i : nodesToRemove) {
			ant.nodesToVisit.remove(i);
		}
	}

    @Override
    public void setWeightCost(Ant ant){
        for (Integer i : ant.tour) {

            weightCost += cost[i];
        }

    }
    public double getWeightCost(){
        return weightCost;
    }
	@Override
	public double evaluate(Ant ant) {
		double sum = 0.0;

		for (Integer i : ant.tour) {

			sum += satisfaction[i];
			//sum += cost[i];
		}

		return sum;
	}


	@Override
	public boolean better(Ant ant, Ant bestAnt) {

            return bestAnt == null || ant.tourLength > bestAnt.tourLength;
	}



	@Override
	public double getDeltaTau(Ant ant, int i, int j) {
		return ant.tourLength / satR;
	}


}
