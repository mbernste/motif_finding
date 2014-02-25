package em;

import java.util.HashMap;
import java.util.Map;

import data.SequenceSet;

/**
 * Stores the matrix of probabilities for the hidden data.  In the motif
 * learning task, the hidden data is the starting positions of the motifs
 * in each sequence.  The element at the ith row, jth column of the matrix
 * is the probability that position j of is the first residue of the motif in
 * sequence i.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class MotifLocations 
{
	/**
	 * Map a sequence to an array of start location probabilities
	 */
	Map<String, Double[]> locationProbabilities;
	
	public MotifLocations(SequenceSet sequences, Motif motif)
	{		
		locationProbabilities = new HashMap<String, Double[]>();
		
		for (String sequence : sequences.getSequences())
		{
			Double[] probabilities = new Double[sequence.length() - motif.getWidth() + 1];
			locationProbabilities.put(sequence, probabilities);
		}
	}
	
	/**
	 * Set the location probability for a target character in a target sequence
	 * 
	 * @param sequence index of the target sequence
	 * @param position index of the character in the target sequence
	 * @param probability the probability the motif begins at this target 
	 * character in this target sequence
	 */
	public void setLocationProbability(String sequence, 
									   int position, 
									   double probability)
	{
		locationProbabilities.get(sequence)[position] = probability;
	}
	
	public double getLocationProbability(String sequence, int position)
	{
		return locationProbabilities.get(sequence)[position];
	}
	
	public void normalize()
	{
		for (Double[] probabilities : locationProbabilities.values())
		{
			double sum = 0.0;
			
			for (int j = 0; j < probabilities.length; j++)
			{
				sum += probabilities[j];
			}
			
			for (int j = 0; j < probabilities.length; j++)
			{
				probabilities[j] /= sum;
			}
		}
	}
	
	@Override
	public String toString()
	{
		String result = "";

		for (Double[] probabilities : locationProbabilities.values())
		{ 
			for (int j = 0; j < probabilities.length; j++)
			{
				result += probabilities[j] + "\t";
			}
			result += "\n";
		}
		
		return result;
	}
	
	public double sumOfAllProbabilities()
	{
		double sum = 0.0;
		
		for (Double[] probabilities : locationProbabilities.values())
		{
			for (int j = 0; j < probabilities.length; j++)
			{
				sum += probabilities[j];
			}
		}
		
		return sum;
	}
	
	public Integer maxProbabilityStartPosition(String sequence)
	{
		double maxZ = 0.0;
		Integer location = null;
		
		Double[] probs = locationProbabilities.get(sequence);
		
		for (int i = 0; i < probs.length; i++)
		{
			if (probs[i] > maxZ) 
			{
				maxZ = probs[i];
				location = i;
			}
		}
				
		return location;
	}
	
}
