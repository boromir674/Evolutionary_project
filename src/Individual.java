import java.util.Random;


public abstract class Individual implements Comparable<Individual>{

	protected double[] genotype;
	private int dim; //dimensions
	private double fitness; 
	static double epsilon = 0.01;
	private int age;
	final static int lowerBound = -5;
	final static int upperBound = -5;

	public Individual(int dim, int genotypeLength) {
		this.dim = dim;
		this.genotype = new double[genotypeLength];
		age = 0;
	}

	public int getDim(){
		return this.dim;
	}
	
	public double[] getXValues(){
		double[] xs = new double[dim];
		// double[] xs = Arrays.copyOfRange(genotype, 0, dim);
		for (int i = 0; i < dim; i++)
			xs[i] = genotype[i];
		return xs;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	protected void updateAge(){
		this.age ++;
	}

	public abstract Individual mutate(Random aRandom);
}
