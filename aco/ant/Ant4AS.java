package mariam.aco.ant;

import java.util.ArrayList;

import mariam.util.RouletteWheel;
import mariam.aco.ACO;

/**
 * The AS Ant Class
 *
 * @author Thiago Nascimento
 * @since 2014-07-27
 * @version 1.0
 */
public class Ant4AS extends Ant {

    public Ant4AS(ACO aco) {
        super(aco);
    }

    @Override
    public void explore() {
        while (!nodesToVisit.isEmpty()) {
            int nextNode = doExploration(currentNode);

            //Save next node
            tour.add(new Integer(nextNode));
            path[currentNode][nextNode] = 1;
            path[nextNode][currentNode] = 1;

            aco.p.updateTheMandatoryNeighborhood(this);

            currentNode = nextNode;
        }
    }

    /**
     * Return the next node
     *
     * @param i The current node
     * @return The next node
     */
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
        Ant ant = new Ant4AS(aco);
        ant.id = id;
        ant.currentNode = currentNode;
        ant.tourLength = tourLength;
        ant.tour = new ArrayList<Integer>(tour);
        ant.path = path.clone();
        return ant;
    }
}
