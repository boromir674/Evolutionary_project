import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

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
		// TODO Auto-generated method stub
		System.out.println("From main");
		double f = 10 - 10*( ( 20-0) / 11.5356 ) ;
		System.out.println(f);
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
		int pop_size = 1000;
		double init_sigma = Math.sqrt(2);
		Population pop = new Population(pop_size, 1, init_sigma, this.rnd_);
		
		int evals = 0;
		while(evals<evaluations_limit_){
			// Select parents
			// Apply variation operators and get children
			//	double child[] = ...
			for (int i=0; i<pop_size; i++){
				Double fitness = (Double) evaluation_.evaluate(pop.getX(i));
				//System.out.println(fitness);
			}
			pop.mutate();
			
			evals++;
			// Select survivors
		}
	}
}
