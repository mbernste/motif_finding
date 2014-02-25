package meme;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents the motif model assuming One Occurrence Per Sequence (OOPS). 
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Motif 
{
	/**
	 * The width of the motif
	 */
	private int width;
	
	/**
	 * The alphabet composing the motif
	 */
	private ArrayList<Character> alphabet;
	
	
	/**
	 * The matrix holding the probability of each symbol appearing at a given
	 * position of the motif, where index (i,j) holds the probability of the
	 * ith symbol of the element occuring at the jth position of the motif.
	 */
	private Double[][] pMatrix;
	
	/**
	 * Copy constructor
	 * 
	 * @param original the original motif
	 */
	public Motif(Motif original)
	{
		this.width = original.width;
		this.pMatrix = new Double[original.pMatrix.length][width + 1];
		this.alphabet = new ArrayList<Character>();
		
		for (Character symbol : original.getAlphabet())
		{
			alphabet.add(symbol);
		}
		
		for (int i = 0; i < original.pMatrix.length; i++)
		{
			for (int j = 0; j < original.pMatrix[i].length; j++)
			{
				this.pMatrix[i][j] = original.pMatrix[i][j];
			}
		}
	}
	
	public Motif(String subsequence, String alphabetStr, Double param)
	{
		this.width = subsequence.length();
		this.pMatrix = new Double[alphabetStr.length()][width + 1];
		this.alphabet = new ArrayList<Character>();

		
		for (int i = 0; i < alphabetStr.length(); i++)
		{
			alphabet.add(alphabetStr.charAt(i));
		}
		
		/*
		 * Generate random values for motif
		 */
		for (int i = 0; i < pMatrix.length; i++)
		{
			/*
			 *  Initial background probabilities
			 */
			pMatrix[i][0] = 1.0 / alphabetStr.length();
			
			/*
			 *  Initial motif probabilities
			 */
			for (int j = 1; j < pMatrix[i].length; j++)
			{
				if (alphabet.get(i) == subsequence.charAt(j-1))
				{
					pMatrix[i][j] = param;
				}
				else
				{
					pMatrix[i][j] = (1 - param) / (alphabetStr.length() - 1);
				}
			}
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param width the width of the Motif
	 * @param alphabetStr the String encoding the alphabet
	 */
	public Motif(int width, String alphabetStr)
	{
		this.width = width;
		this.pMatrix = new Double[alphabetStr.length()][width + 1];
		this.alphabet = new ArrayList<Character>();
		
		for (int i = 0; i < alphabetStr.length(); i++)
		{
			alphabet.add(alphabetStr.charAt(i));
		}
		
		/*
		 * Generate random values for motif
		 */
		Random rand = new Random();
		for (int i = 0; i < pMatrix.length; i++)
		{
			for (int j = 0; j < pMatrix[i].length; j++)
			{
				pMatrix[i][j] = rand.nextDouble();
			}
		}
		
		/*
		 * Normalize the values to create random probabilities
		 */
		for (int j = 0; j < width + 1; j++)
		{
			double sum = 0.0;
			for (int i = 0; i < pMatrix.length; i++)
			{
				sum += pMatrix[i][j];
			}
			
			for (int i = 0; i < pMatrix.length; i++)
			{
				pMatrix[i][j] /= sum;
			}
		}
	}
	
	/**
	 * Return the probability of a given symbol occurring at a given position 
	 * in the motif
	 * 
	 * @param symbol the target symbol
	 * @param position the target position in the motif
	 * @return the probability of the target symbol appearing at the target 
	 * position in the motif
	 */
	public Double getProbability(char symbol, int position)
	{
		int row = alphabet.indexOf(symbol);
		return pMatrix[row][position];
	}
	
	/**
	 * Set the probability of a given symbol occurring at a given position in 
	 * the motif
	 * 
	 * @param symbol the target symbol
	 * @param position the target position in the motif
	 * @param probability the probability of the target symbol appearing at the 
	 * target position in the motif
	 */
	public void setProbability(char symbol, int position, double probability)
	{
		int row = alphabet.indexOf(symbol);		
		pMatrix[row][position] = probability;
	}
	
	/**
	 * @return the width of the motif
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		/*
		for (int i = 0; i < width + 1; i++)
		{
			result += i + "\t";
		}
		result += "\n";
		*/
		
		for (int i = 0; i < alphabet.size(); i++)
		{
			//result += alphabet.get(i) + "\t";
			for (int j = 0; j < width + 1; j++)
			{
				result += pMatrix[i][j] + "\t";
			}
			result += "\n";
		}
		
		return result;
	}
	
	/**
	 * @return this motif's alphabet
	 */
	public ArrayList<Character> getAlphabet()
	{
		return this.alphabet;
	}
	
}
