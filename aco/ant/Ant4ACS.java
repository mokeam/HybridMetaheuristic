/*
 * Copyright 2014 Thiago Nascimento
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mariam.aco.ant;

import java.util.ArrayList;

import mariam.sys.Constants;
import mariam.util.PseudoRandom;
import mariam.aco.ACO;
import mariam.util.RouletteWheel;


public class Ant4ACS extends Ant4AS {
	
	/** Probability of best choice in tour construction */
	protected double Q0 = Constants.Q0;
	
	/** Decrease local pheromone */
	protected double P = Constants.P;
	
	public Ant4ACS(ACO aco) {
		super(aco);		
	}

	@Override
	public void explore() {
		while (!nodesToVisit.isEmpty()) {
			int nextNode = -1;

//			if (PseudoRandom.randDouble(0, 1) <= Q0) {
//				nextNode = doExploitation(currentNode); //for improving and refining the existing solution / local search
//			}
//            else {

				nextNode = doExploration(currentNode);  //finding other promising solutions
			//}

			localUpdateRule(currentNode, nextNode);

			// Save next node
			tour.add(new Integer(nextNode));
			path[currentNode][nextNode] = 1;
			path[nextNode][currentNode] = 1;

			aco.p.updateTheMandatoryNeighborhood(this);

			currentNode = nextNode;
		}
	}

	protected void localUpdateRule(int i, int j) {
		double evaporation = (1.0 - P) * aco.getEdge(i, j);
		double deposition = P * aco.p.getT0();
		
		aco.setEdge(i, j, evaporation + deposition);
		aco.setEdge(j, i, evaporation + deposition);
	}


	/**
	 * Return the next node
	 *
	 * @param i The current node
	 * @return The next node
	 */
	protected int doExploitation(int i) {
		int nextNode = -1;
		double maxValue = Double.MIN_VALUE;

        //calculates the probability of moving to every unvisited neighboring nodes and returns the node with the highest probability
		// Update the sum
		for (Integer j : nodesToVisit) {
			if (aco.getEdge(i, j) == 0.0) {
				throw new RuntimeException("edge == 0.0");
			}

			double tij = aco.getEdge(i, j);
			double nij = Math.pow(aco.p.getNij(i, j), BETA);
			double value = tij * nij;  //pheromone value
			
			if(value > maxValue){
				maxValue = value;
				nextNode = j;
			}
		}
		
		if (nextNode == -1) {
			throw new RuntimeException("nextNode == -1");
		}

		nodesToVisit.remove(new Integer(nextNode));

		return nextNode;
	}

    protected int doExploration(int i) {
        int nextNode = -1;
        double sum = 0.0;

        // Update the sum
        for (Integer j : nodesToVisit) {
            if (aco.getEdge(i, j) == 0.0) {
                throw new RuntimeException("edge == 0.0");
            }

            double tij = Math.pow(aco.getEdge(i, j), ALPHA);
            double nij = Math.pow(aco.p.getNij(i, j), BETA);
            sum += tij * nij;
        }

        if (sum == 0.0) {
            throw new RuntimeException("sum == 0.0");
        }

        double[] probability = new double[aco.p.getNoOfRequirements()];
        double sumProbability = 0.0;

        for (Integer j : nodesToVisit) {
            double tij = Math.pow(aco.getEdge(i, j), ALPHA);
            double nij = Math.pow(aco.p.getNij(i, j), BETA);
            probability[j] = (tij * nij) / sum;
            sumProbability += probability[j];
        }

        // Select the next node by probability
        nextNode = RouletteWheel.select(probability, sumProbability);

        if (nextNode == -1) {
            throw new RuntimeException("nextNode == -1");
        }

        nodesToVisit.remove(new Integer(nextNode));

        return nextNode;
    }

	@Override
	public Ant clone() {
		Ant ant = new Ant4ACS(aco);
		ant.id = id;
		ant.currentNode = currentNode;
		ant.tourLength = tourLength;
		ant.tour = new ArrayList<Integer>(tour);
		ant.path = path.clone();
		return ant;
	}
}