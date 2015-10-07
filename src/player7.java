import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Properties;

public class player7 implements ContestSubmission
{
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	public player7()
	{
		rnd_ = new Random();
	}

	public static void main(String[] args) {

		player7 ap = new player7();
		SphereEvaluation as = new SphereEvaluation();
		RastriginEvaluation re = new RastriginEvaluation();
		FletcherPowellEvaluation fe = new FletcherPowellEvaluation();
		ap.setSeed(1);
		ap.setEvaluation(as);

		int pop_size = 10;
		double init_sigma = 0.1;//Math.sqrt(2);
		Population pop = new Population(pop_size, 10, 1, init_sigma, ap.rnd_);

		int evals = 0;
		while(evals < 10000){
			// Select parents
			// Apply variation operators and get children
			//	double child[] = ...
			pop.rankingSelection(7*pop_size);
			pop.mutationStep();
			evals++;
			System.out.println(evals);
			pop.mCommaLSurvivorSelection();
		}

	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
		evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
		boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
		boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Change settings(?)
		if(isMultimodal){
			// Do sth
		}else{
			// Do sth else
		}
	}

	public void run()
	{
		System.out.println("From run");

		// Run your algorithm here
		// Initialize population
		int pop_size = 100;
		double init_sigma = Math.sqrt(2);
		//Population pop = new Population(pop_size, 1, init_sigma, this.rnd_);

		int evals = 0;
		while(evals < evaluations_limit_){
			// Select parents
			// Apply variation operators and get children
			//	double child[] = ...
			//pop.mutate();
			for (int i=0; i < pop_size; i++){
				//Double fitness1 = (Double) evaluation_.evaluate(pop.getX(i));
				//Double fitness2 = (Double) evaluation_.evaluate(pop.getXChildren(i));
				//pop.setCurrentFitness(i, fitness1);
				//pop.setChildrenFitness(i, fitness2);

			}

			//pop.selectSurvivors();
			evals++;
			System.out.println(evals);
			// Select survivors
		}
	}
}
