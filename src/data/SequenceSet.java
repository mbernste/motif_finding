package data;

import java.util.ArrayList;

import meme.Motif;
import meme.MotifLocations;

import pair.Pair;


/**
 * Represents a set of sequences.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 * 
 */
public class SequenceSet 
{
	/**
	 * The sequences
	 */
	private ArrayList<String> sequences;
	
	/**
	 * Constructor
	 */
	public SequenceSet()
	{
		sequences = new ArrayList<String>();
	}
	
	/**
	 * @return the sequences in this sequence set
	 */
	public ArrayList<String> getSequences()
	{
		return this.sequences;
	}
	
	/**
	 * Add a sequence to this sequence set
	 * 
	 * @param sequence the new sequence
	 */
	public void addSequence(String sequence)
	{
		sequences.add(sequence);
	}
	
	/**
	 * @return the number of sequences in this sequence set
	 */
	public int getNumSequences()
	{
		return sequences.size();
	}
	
	/**
	 * Retrieve a sequence from the sequence set
	 * 
	 * @param index the index of the sequence to be retrieved
	 * @return the target sequence
	 */
	public String getSequence(int index)
	{
		return sequences.get(index);
	}
	
	/**
	 * Count the expected number of times a symbol appears at a given position 
	 * of the motif in all of the sequences.
	 * 
	 * @param symbol the target symbol
	 * @param position the position of the motif in which we would like to count
	 * the expected number of appearances of the target symbol
	 * @param motifWidth the width of the motif
	 * @param z the expected starting locations of the motif in the sequences
	 * @return the expected number of times a symbol appears at a given position 
	 * of the motif in all of the sequences
	 */
	public double countSymbolAtMotifPosition(char symbol, 
											 int position, 
											 int motifWidth, 
											 MotifLocations z)
	{
		double count = 0;
				
		for (String sequence : sequences)
		{			
			for (int j = position - 1; j < sequence.length() - motifWidth + position; j++)
			{
				if (sequence.charAt(j) == symbol)
				{					
					count += z.getLocationProbability(sequence, j-(position-1));
				}
			}
		}
		
		return count;
	}
	

	/**
	 * Calculate the expected count of a specific symbol at a given position of 
	 * the motif using the location probabilities of the motif in every 
	 * sequence.
	 *  
	 * @param symbol the target symbol
	 * @param position the position within the motif
	 * @param motifWidth the width of the motif
	 * @param z the expected motif locations in the sequences
	 * @return the expected count of the target symbol being at the target
	 * position of the motif
	 */
	public double totalCountAtMotifPosition(char symbol, 
											int position, 
											int motifWidth, 
											MotifLocations z)
	{
		double count = 0;
		
		for (String sequence : sequences)
		{			
			for (int j = position - 1; j < sequence.length() - motifWidth + position; j++)
			{
				count += z.getLocationProbability(sequence,j-(position-1));
			}
		}
		
		return count;
	}
	
	/**
	 * Count a symbol for all sequences in this sequence set
	 * 
	 * @param symbol the symbol to be counted
	 * @return the total count of the target symbol
	 */
	public double countSymbol(char symbol)
	{
		double count = 0;
		
		for (String sequence : sequences)
		{
			for (int i = 0; i < sequence.length(); i++)
			{
				if (sequence.charAt(i) == symbol)
				{
					count++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Find the subsequence in each sequence that is most likely to be the
	 * sequence in the motif.
	 * 
	 * @param p the motif model matrix
	 * @param z the probability of the motif start positions
	 * @return a list of pairs where each pair stores the index of the 
	 * motif in a sequence along with the subsequence
	 */
	public ArrayList<Pair<Integer, String>> pickOutMotifs(Motif p, 
														  MotifLocations z)
	{	
		ArrayList<Pair<Integer, String>> result
									= new  ArrayList<Pair<Integer, String>>();
		
		for (String sequence : sequences)
		{
			int start = z.maxProbabilityStartPosition(sequence);
						
			String realizedMotif = sequence.substring(start, start+p.getWidth());
			result.add( new Pair<Integer, String>(start, realizedMotif));
		}
		
		return result;
	}
}
