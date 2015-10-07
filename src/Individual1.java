import java.util.Random;


public class Individual1 extends Individual {
	// genotype : <x1, x2, ..., xn, σ>
	private double t;


	public Individual1(int dim, double sigma, Random aRandom){
		super(dim, dim+1);
		t = 1/Math.sqrt(dim);

		for (int i=0; i<dim; i++)
			genotype[i] = 10 * aRandom.nextDouble() - 5;
		genotype[dim] = sigma;
	}

	protected Individual1(Individual i1, Individual i2, double sigma){
		super(i1.getDim(), i1.getDim()+1);		
		t = 1/Math.sqrt(super.getDim());
		for (int i=0; i<super.getDim(); i++)
			genotype[i]= (i1.genotype[i] + i2.genotype[i]) / 2;		
		genotype[super.getDim()] = sigma;
	}

	private Individual1(Individual i1){
		super(i1.getDim(), i1.getDim()+1);
		t = 1/Math.sqrt(i1.getDim());
		for (int i=0; i<i1.getDim()+1; i++)
			genotype[i] = i1.genotype[i];
	}

	@Override
	public Individual1 mutate(Random aRandom) { // uncorrelated mutation with 1 σ
		int d = this.getDim();
		Individual1 newIndividual = new Individual1(this);
		double temp = aRandom.nextDouble(); // N(0,1)
		newIndividual.genotype[d] *= Math.exp(t * temp); // mutate σ first
		if (newIndividual.genotype[d] < epsilon) // check if σ'<ε
			newIndividual.genotype[d] = epsilon;
		for (int i=0; i<d; i++) {// update x vector
			newIndividual.genotype[i] += newIndividual.genotype[d] * aRandom.nextDouble();
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
