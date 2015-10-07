import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.vu.contest.ContestEvaluation;


public class Population {

	private final int popSize; // population size
	private ArrayList<Individual> pop; // population
	private final int dim; // dimensions (10)
	private double sigma;
	private int generationCount;
	private int type;
	private Random popRandom;
	private int[] matingPool;
	private ContestEvaluation evaluator;

	public Population(int size, int dim, int type, double sigma, Random aRandom){
		this.popSize = size;
		this.dim = dim;
		this.sigma = sigma;
		this.generationCount = 0;
		this.type = type;
		this.popRandom = aRandom;

		switch (type){
		case 1:
			pop = new ArrayList<Individual>();
			for (int i=0; i<this.popSize; i++){
				Individual1 temp = new Individual1(dim, sigma, aRandom);
				this.addIndividual(temp);
			}
			break;
		case 2:
			pop = new ArrayList<Individual>();
			for (int i=0; i<this.popSize; i++){
				Individual2 temp = new Individual2(dim, sigma, aRandom);
				this.addIndividual(temp);
			}
			break;
		case 3:
			break;
		}
	}
	protected int getTempSize(){
		return pop.size();
	}
	protected Individual getAnIndividual(int index){
		return this.pop.get(index);
	}
	private void replaceIndividual(int position, Individual newMember){
		this.pop.set(position, newMember);		
	}
	private void addIndividual(Individual newMember){
		double fitness = (double) this.evaluator.evaluate(newMember.getXValues());
		newMember.setFitness(fitness);
		this.pop.add(newMember);
	}
	private void updateGenerationCount(){
		this.generationCount ++;
	}
	private double getSumOfFitnessValues(){// calculate the sum of all fitness values
		double sum = 0.0;
		for (int i=0; i<pop.size(); i++)
			sum += this.getAnIndividual(i).getFitness();
		return sum;
	}
	private double[] getCumulativeDistribution(double sumOfFitnessValues){
		// assuming a sorted population

		// check if minimum value is negative and apply transformation if necessary
		double minFitness = this.getAnIndividual(pop.size()-1).getFitness();
		double transformedSum = sumOfFitnessValues;
		if (minFitness < 0)
			transformedSum += pop.size()*Math.abs(minFitness);
		else
			minFitness = 0;		
		double cumulativeProbs[] = new double[pop.size()];

		cumulativeProbs[0] = (getAnIndividual(pop.size()-1).getFitness()-minFitness) / (transformedSum);
		for (int i=1; i<pop.size(); i++){ // builds the cumulative probability distribution
			cumulativeProbs[i] = cumulativeProbs[i-1] + (getAnIndividual(pop.size()-1-i).getFitness()-minFitness) / transformedSum;
		}
		return cumulativeProbs;
	}
	private double[] findRankingProbs(){
		// assuming a sorted population
		double s = 1.5; // parameter: 1 < s <= 2
		double[] probs = new double[pop.size()];
		for (int i=0; i<pop.size(); i++) // linear formula
			probs[i] = (2.0 - s)/popSize + 2*i*(s-1)/(popSize*(popSize-1));
		return probs;
	}
	private Individual findBest(){
		double maxFitness = Double.MIN_VALUE;
		int pointer = 0;
		for (int i=0; i<pop.size(); i++)
			if (this.getAnIndividual(i).getFitness() > maxFitness)
				pointer = i;
		return this.getAnIndividual(pointer);
	}

	private Individual recombine(Individual i1, Individual i2){
		Individual tempInd = null;
		switch (type) {
		case 1:
			tempInd = new Individual1(i1, i2, this.sigma);
			break;
		case 2:
			tempInd = new Individual2(i1, i2, this.sigma);
			break;
		case 3:
			break;
		}
		return tempInd;
	}

	public void EPSchema(){ // all parents get mutated and replaced by their offsprings
		Individual tempIndividual;
		for (int i=0; i<this.pop.size(); i++){
			tempIndividual = this.getAnIndividual(i).mutate(popRandom);
			this.replaceIndividual(i, tempIndividual);
			double fitness = (double) this.evaluator.evaluate(tempIndividual.getXValues());
			tempIndividual.setFitness(fitness);
		}
	}
	
	// PARENT SELECTION METHODS
	// ------------------------
	
	// Uniform Sample Selection
	public void uniformSelection(int lambda){// requires strong fitness-based survivor selection
		// stochastically selects lambda parents, sampling uniformly
		matingPool = new int[lambda];
		for (int i=0; i<lambda; i++){
			matingPool[i] = popRandom.nextInt(popSize);
		}
	}
	// "Fitness Proportional Selection"
	public void fpsParentSelection(int lambda){
		// stochastically selects lambda parents, sampling according to probabilities based
		// on the fitness values of each member of the population

		Collections.sort(this.pop); // sorts population according to fitness value
		double sumOfFitnessValues = this.getSumOfFitnessValues();
		double cumulativeProbs[] = getCumulativeDistribution(sumOfFitnessValues);

		// array with indices to the pop ArrayList
		matingPool = Utils.stochasticUniversalSampling(cumulativeProbs, lambda, popRandom);
		//matingPool = Utils.rouletteWheel(cumulativeProbs, lambda, popRandom);	
	}

	// "Ranking Selection"
	public void rankingSelection(int lambda){
		// stochastically selects lambda parents, sampling according to probabilities based
		// on the linear rank of each member of the population

		Collections.sort(this.pop); // sorts population according to fitness value
		double[] rankProbabilities = this.findRankingProbs(); // probabilities based on ranking
		// array with indices to the pop ArrayList
		matingPool = Utils.stochasticUniversalSampling(rankProbabilities, lambda, popRandom);
		//matingPool = Utils.rouletteWheel(rankProbabilities, lambda, popRandom); 
	}

	// OPERATORS

	// Mutation Step
	public void mutationStep(){
		for (int i=0; i<matingPool.length; i++){
			Individual newIndividual = getAnIndividual(matingPool[i]).mutate(popRandom);
			this.addIndividual(newIndividual);
		}		
	}

	// Crossover and Mutation Step
	public void crossoverAndMutateStep(){
		for (int i=0; i<matingPool.length; i++){
			Individual i1 = getAnIndividual(matingPool[i]);
			int pick = this.popRandom.nextInt(matingPool.length);
			if (matingPool[pick] == matingPool[i])
				pick ++;
			pick = pick % matingPool.length;
			Individual i2 = getAnIndividual(matingPool[pick]);
			Individual newIndividual = recombine(i1, i2);
			newIndividual.mutate(popRandom);
			this.addIndividual(newIndividual);
		}
	}
	
	// SURVIVOR SELECTION METHODS
	// -------------------------

	// Round-Robin tournament
	public void roundRobin(){

	}

	// "Fitness-Based Replacement" (μ+λ)
	public void mPlusLSurvivorSelection(){
		// applies (μ+λ) survivor selection and updates the population

		Collections.sort(this.pop); // sorts population according to fitness value
		this.pop.subList(this.popSize, pop.size()).clear(); // keeps top μ members
		this.updateGenerationCount();
	}

	// "Fitness-Based Replacement" (μ,λ)
	public void mCommaLSurvivorSelection(){ // preferred over (μ+λ)
		// applies (μ,λ) survivor selection and updates the population
		this.pop.subList(0, popSize).clear(); // discard parents (top μ members) 
		Collections.sort(this.pop); // sorts population according to fitness value
		this.pop.subList(this.popSize, pop.size()).clear(); // keeps top μ members
		this.updateGenerationCount();
	}

	// "Fitness Proportional Selection with elitism"
	public void fpsSurvivorSelection(){
		// stochastically selects survivors, sampling according to probabilities based
		// on the fitness values of each member of the population while preserving the
		// top performing individual at every generation
		// Updates the population

		Collections.sort(this.pop); // sorts population according to fitness value
		Individual oldBest = this.getAnIndividual(0); // member with best fitness value

		double sumOfFitnessValues = this.getSumOfFitnessValues();
		double cumulativeProbs[] = getCumulativeDistribution(sumOfFitnessValues);

		// array with indices pointing to members for keeping
		int[] survivors = Utils.rouletteWheel(cumulativeProbs, popSize, popRandom);

		// store members picked by the roulette at the top μ positions
		for (int i=0; i<popSize; i++)
			replaceIndividual(i, getAnIndividual(survivors[i]));
		this.pop.subList(this.popSize, pop.size()).clear(); // discard all after top μ

		// ensure that best member is kept unless an even better survivor already exists
		Individual bestMember = this.findBest();
		if (oldBest.getFitness() < bestMember.getFitness()){ // member with best fitness is not selected
			//then force it to the nest generation
			this.replaceIndividual(0, oldBest);
		}		
		this.updateGenerationCount();
	}
}
