package meme.oops;

import meme.Counts;
import meme.Motif;
import meme.MotifLocations;
import data.SequenceSet;
import pair.Pair;

/**
 * The Expectation Maximization Algorithm for learning the OOPS model in 
 * set of sequences.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class MEME_OOPS 
{
	/**
	 * The alphabet of DNA sequences
	 */
	public static final String DNA_ALPHABET = "ACGT";

	/**
	 * The laplace count to use in all probability count estimations
	 */
	private static final int PSUEDOCOUNT = 1;

	/**
	 * The minimum change in log probability of data given model parameters for
	 * the stopping criteria to be met
	 */
	private static final double EPSILON = 0.001;

	/**
	 * Number of iterations for which to run the EM algorithm on each 
	 * candidate starting parameter
	 */
	private static final int STARTING_POINT_ITERATIONS = 3;
	
	/**
	 * Probability mass to place on each symbol of a subsequence in each
	 * position of the motif for the starting parameters
	 */
	private static final double START_PARAM = 0.6;

	/**
	 * Run the Expecation Maximization Algorithm on a set of sequence to 
	 * find the motif and motif location in each sequence that locally maximizes
	 * the probability of the sequences.
	 * 
	 * @param sequences the sequences in the data set
	 * @return the motif model and motif locations that locally maximize the
	 * probability of the sequences
	 */
	public static Pair<Motif, MotifLocations> run(SequenceSet sequences, 
			int motifWidth)
	{
		Pair<Motif, MotifLocations> result = new Pair<Motif, MotifLocations>();
		MotifLocations z = null;
		
		Motif p = getStartingMotif(sequences, motifWidth, DNA_ALPHABET, STARTING_POINT_ITERATIONS);
		
		/*
		 *  Repeat E-Step & M-Step until convergence
		 */
		double probData = 1.0;
		double prevProbData = 0.0;
		while (Math.abs(probData - prevProbData) > EPSILON)
		{
			z = eStep(sequences, p);
			p = mStep(sequences, z, motifWidth);

			prevProbData = probData;
			probData = probabilityOfData(sequences, p, z);			
		}

		result.setFirst(p);
		result.setSecond(z);
		
		return result;
	}


	public static Motif getStartingMotif(SequenceSet sequences,
			int width, 
			String alphabet, 
			int numTries)
	{
		Motif bestMotif = null;
		double bestProbData = Integer.MAX_VALUE;

		for (String sequence : sequences.getSequences())
		{
			for (int i = 0; i < sequence.length() - width; i++)
			{				
				String subSeq = sequence.substring(i, i + width);
								
				Motif currP = new Motif(subSeq, alphabet, START_PARAM);
								
				Motif p = new Motif(currP);
				MotifLocations z = null;
				for (int j = 0; j < numTries; j++)
				{
					z = eStep(sequences, p);
					p = mStep(sequences, z, width);
				}
								
				double probData = probabilityOfData(sequences, p, z);
				if (probData < bestProbData)
				{
					bestProbData = probData;
					bestMotif = currP;
				}
			}
		}
		
		return bestMotif;
	}
	
	/**
	 * The Expectation-Step of the EM Algorithm for the OOPS motif finding 
	 * problem, we calculate the expected value of the hidden of the data: the
	 * motif starting positions in each sequence based on previously calculated
	 * motif probabilities in the M-Step.
	 * 
	 * @param sequences the sequences comprising the data set
	 * @param p the motif model
	 * @return the new estimates of the motif locations in the sequences
	 */
	public static MotifLocations eStep(SequenceSet sequences, Motif p)
	{
		MotifLocations z = new MotifLocations(sequences, p);

		/*
		 *  Iterate through all sequences
		 */
		for (String sequence : sequences.getSequences())
		{
			/*
			 *  Iterate through each sequence x_i, and calculate the probability
			 *  of the sequence given the starting position of the motif is
			 *  each position of the sequence:
			 *  
			 *  P(x_i | z_i,j , motif)
			 */
			for (int j = 0; j < sequence.length() - p.getWidth() + 1; j++)
			{				
				double pSequence = probabilityOfSequence(sequence, p, j);
				z.setLocationProbability(sequence, j, pSequence);
			}
		}

		/*
		 *	Normalize the probability estimates of the hidden data  
		 */
		z.normalize();

		return z;
	}

	public static Motif mStep(SequenceSet sequences, 
			MotifLocations z,
			int width)
	{
		/*
		 * The updated motif probabilities
		 */
		Motif p = new Motif(width, DNA_ALPHABET);

		/*
		 * Generate all expected counts
		 */
		Counts counts = new Counts(sequences, z, p);

		/*
		 * Calculate new parameters
		 */
		for (Character symbol : p.getAlphabet())
		{				
			/*
			 * Calculate motif probabilities
			 */
			for (int k = 1; k < p.getWidth() + 1; k++)
			{
				double numerator = counts.getCountAtPosition(symbol, k) + PSUEDOCOUNT;
				double denominator = z.sumOfAllProbabilities() + (DNA_ALPHABET.length() * PSUEDOCOUNT);
				double updatedProbability = numerator / denominator;
				p.setProbability(symbol, k, updatedProbability);
			}

			/*
			 * Calculate background probabilities
			 */
			double numerator = counts.getBGCount(symbol) + PSUEDOCOUNT;
			double denominator = counts.sumAllBgCounts() + (DNA_ALPHABET.length() * PSUEDOCOUNT);
			double updatedProbability = numerator / denominator;
			p.setProbability(symbol, 0, updatedProbability);
		}

		return p;
	}


	/**
	 * Calculate the probability of a sequence given a motif model and position
	 * of the motif in the sequence
	 * 
	 * @param sequence the target sequence
	 * @param motif the model of the motif in the sequence
	 * @param startPosition the position of the first character of the motif
	 * in the sequence
	 * @return the total probability of the sequence
	 */
	public static double probabilityOfSequence(String sequence, 
			Motif motif, 
			int startPosition)
	{
		double probability = 1.0;

		/*
		 * Background probabilities for residues before the motif
		 */
		for (int i = 0; i < startPosition; i++)
		{
			probability *= motif.getProbability(sequence.charAt(i), 0);
		}

		/*
		 * Motif probabilities for residues within the motif
		 */
		for (int i = 0; i < motif.getWidth(); i++ )
		{			
			int motifPosition = i + 1;
			probability *= motif.getProbability( sequence.charAt(i + startPosition), 
					motifPosition );	
		}


		/*
		 * Background probabilities for residues after the motif
		 */
		for (int i = startPosition + motif.getWidth(); i < sequence.length(); i++)
		{
			probability *= motif.getProbability(sequence.charAt(i), 0);
		}

		return probability;
	}
	
	public static Double probabilityOfData(SequenceSet sequences, 
										   Motif p, 
										   MotifLocations z)
	{
		double probData = 0.0;
		
		for (String sequence : sequences.getSequences())
		{
			double probSeq = 0.0;
			for (int k = 0; k < sequence.length() - p.getWidth(); k++)
			{
				probSeq += probabilityOfSequence(sequence, p, k) * z.getLocationProbability(sequence, k);
			}
			probSeq = -Math.log(probSeq);
			probData += probSeq;
		}
		
		return probData;
	}
}
