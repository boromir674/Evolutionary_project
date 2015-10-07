import java.util.Random;


public class Individual2 extends Individual {
	// genotype : <x1, x2, ..., xn, σ1, σ2, ..., σn>
	private double tPrime;
	private double t;


	public Individual2(int dim, double sigma, Random aRandom){
		super(dim, 2*dim);
		tPrime = 1/Math.sqrt(2*dim);
		t = 1/Math.sqrt(2*Math.sqrt(dim));

		for (int i=0; i<dim; i++){
			genotype[i] = 10 * aRandom.nextDouble() - 5;
			genotype[i+dim] = sigma;
		}
	}
	public Individual2(Individual i1, Individual i2, double sigma){
		super(i1.getDim(), 2*i1.getDim());		
		tPrime = 1/Math.sqrt(2*super.getDim());
		t = 1/Math.sqrt(2*Math.sqrt(super.getDim()));
		
		for (int i=0; i<super.getDim(); i++){
			genotype[i] = (i1.genotype[i] + i2.genotype[i]) / 2;
			genotype[i + super.getDim()] = sigma;
		}
	}
	
	private Individual2(Individual i1){
		super(i1.getDim(), 2*i1.getDim());
		tPrime = 1/Math.sqrt(2*super.getDim());
		t = 1/Math.sqrt(2*Math.sqrt(super.getDim()));
		
		for (int i=0; i<2*super.getDim(); i++)
			genotype[i] = i1.genotype[i];
	}

	@Override
	public Individual2 mutate(Random aRandom) { // uncorrelated mutation with n σ's
		int d = this.getDim();
		Individual2 newIndividual = new Individual2(this);
		double N = aRandom.nextDouble(); // N(0,1)
		
		for (int i=0; i<d; i++){ // update x vector
			double N_i = aRandom.nextDouble();
			newIndividual.genotype[i+d] *= Math.exp(tPrime*N + t*N_i); // mutate σ first
			if (newIndividual.genotype[i+d] < epsilon) // check if σ'<ε
				newIndividual.genotype[i+d] = epsilon;
			newIndividual.genotype[i] += newIndividual.genotype[i+d] * N_i;
			if (newIndividual.genotype[i] < Individual.lowerBound)
				newIndividual.genotype[i] = Individual.lowerBound;
			if (newIndividual.genotype[i] > Individual.upperBound)
				newIndividual.genotype[i] = Individual.upperBound;
		}
		return newIndividual;
	}

	@Override
	public int compareTo(Individual ind1) {
		double compareFitness = ind1.getFitness();
		return (int) (compareFitness - this.getFitness()); // descending order
	}
}
