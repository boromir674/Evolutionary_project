import java.util.Random;


public class Population {

	private final int size;
	private final int type;
	private double[][] table;
	private Random popRandom;
	private double t;
	private double t2;
	private double epsilon = 0.01;

	public Population(int size, int type, double sigma, Random aRandom){
		
		this.size = size;
		this.type = type;
		this.popRandom = aRandom;

		switch (this.type) {

		case 1: // uncorrelated mutation with 1 σ = 5 initially
			this.t = 1/Math.sqrt(10);
			table = new double[size][11];
			for (int i=0; i<this.size; i++)
				table[i][10] = sigma;
			break;			

		case 2: // uncorellated mutation with 10 σ's = 5 initially
			this.t = 1/Math.sqrt(2*10);
			this.t2 = 1/Math.sqrt(2*Math.sqrt(10));
			table = new double[size][20];
			for (int i=0; i<this.size; i++)
				for (int j=10; j<20; j++)
					table[i][j] = sigma;
			break;
			//case 3:
		}

		// uniform random initialization
		for (int i=0; i<this.size; i++){
			for (int j=0; j<10; j++)
				this.table[i][j] = 10 * this.popRandom.nextDouble() - 5;
		}
	}

	public void mutate(){
		// mutates the whole population and immediately replaces it with the children
		switch (this.type) {

		case 1: // uncorrelated mutation with 1 σ
			for (int i=0; i<this.size; i++){ // loop through the population
				double temp = this.popRandom.nextDouble(); // N(0,1)
				this.table[i][10] *= Math.exp(this.t * temp); // mutate σ first
				if (table[i][10] < epsilon) // check if σ'<ε
					table[i][10] = epsilon;
				for (int j=0; j<10; j++) // update x vector
					table[i][j] += table[i][10] * temp;
			}

		case 2: // uncorellated mutation with 10 σ's
			for (int i=0; i<this.size; i++){ // loop through the population
				double temp = this.popRandom.nextDouble(); // N(0,1)
				for (int j=10; j<20; j++){ // loop 10 times
					double temp2 = this.popRandom.nextDouble(); // N_i(0,1)
					table[i][j] *= Math.exp(t2*temp + t2*temp2); // mutate σ_i first
					if (table[i][j] < epsilon) // check if σ'<ε
						table[i][j] = epsilon;
					table[i][j-10] += table[i][j] * temp2; // mutate x vector elementwise
				}
			}
		}
	}
	
	public double[] getX(int index){
		//returns a 10-D vector the x values of the indexed individual
		double[] vector = new double[10];
		for (int j=0; j<10; j++)
			vector[j] = this.table[index][j];
		return vector;
	}
}
