import java.util.Random;


public abstract class Utils {

	public static int[] rouletteWheel(double[] cumulativeProbabilities, int numberOfSamples, Random aRandom){
		double rand;
		int[] indicesSampled = new int[numberOfSamples];
		int j;
		// roulette wheel algorithm
		for (int i=0; i<numberOfSamples; i++){
			rand = aRandom.nextDouble();
			j = 0;
			while (cumulativeProbabilities[j] < rand)
				j++;
			indicesSampled[i] = j;
		}
		return indicesSampled;
	}
	
	public static int[] stochasticUniversalSampling(double[] cumulProbs, int numberOfSamples, Random aRandom){
		double rand = aRandom.nextDouble() / numberOfSamples;
		int[] indicesSampled = new int[numberOfSamples];
		int i = 0;
		int j = 0;
		// stochastic universal sampling algorithm
		while (i<numberOfSamples){
			while (rand <= cumulProbs[j]){
				indicesSampled[i] = j;
				rand += 1/numberOfSamples;
				i ++;
			}
			j ++;
		}
		return indicesSampled;
	}
	
	public static int[] tournamentSelection(int numberOfSamples, int k, Population myPopulation, Random aRandom){
		// tournament selection algorithm
		int tempIndex = 0;
		int winner = 0;
		double max = Double.MIN_VALUE;
		int[] indicesSampled = new int[numberOfSamples];
		
		for (int i=0; i<numberOfSamples; i++){
			for (int j=0; j<k; j++){// with replacement
				tempIndex = aRandom.nextInt(myPopulation.getTempSize());
				if (myPopulation.getAnIndividual(tempIndex).getFitness() > max){
					max = myPopulation.getAnIndividual(tempIndex).getFitness();
					winner = tempIndex;
				}
			}
			indicesSampled[i] = winner;
		}
		return indicesSampled;
	}
}
