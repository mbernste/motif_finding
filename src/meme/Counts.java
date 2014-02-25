package meme;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import data.SequenceSet;

public class Counts 
{
	
	/**
	 * The background counts
	 */
	Map<Character, Double> bgCounts;
	
	/**
	 * The motif counts
	 */
	Map<Character, Double> mCounts;
	
	Map<Character, Double[]> positionCounts;
	
	public Counts(SequenceSet sequences, MotifLocations z, Motif p)
	{
		
		positionCounts = new HashMap<Character, Double[]>();
		bgCounts = new HashMap<Character, Double>();
		mCounts = new HashMap<Character, Double>();
		
		for (Character symbol : p.getAlphabet())
		{
			bgCounts.put(symbol, 0.0);
			mCounts.put(symbol, 0.0);
			positionCounts.put(symbol, new Double[p.getWidth() + 1]);
		}
		
		generateMotifCounts(sequences, z, p);
		generateBackgroundCounts(sequences, z, p);		
	}
	
	private void generateMotifCounts(SequenceSet sequences,
									 MotifLocations z, 
									 Motif p)
	{
		for (Character symbol : p.getAlphabet())
		{
			double count = 0;
			for (int i = 0; i < p.getWidth(); i++)
			{			
				double countAtPosition = sequences.countSymbolAtMotifPosition(symbol, 
						  													  i+1, 
						  													  p.getWidth(), 
						  													  z);
						
				positionCounts.get(symbol)[i+1] = countAtPosition; 
		
				count += countAtPosition;
			}
			mCounts.put(symbol, count);
		}
	}
	
	private void generateBackgroundCounts(SequenceSet sequences,
										  MotifLocations z, 
										  Motif p)
	{
		for (Character symbol : p.getAlphabet())
		{
			double totalCount = sequences.countSymbol(symbol);
			double bgCount = totalCount - mCounts.get(symbol);
			bgCounts.put(symbol, bgCount);
			positionCounts.get(symbol)[0] = bgCount;
		}
	}
	
	public double sumAllBgCounts()
	{
		double sum = 0.0;
		for (Entry<Character, Double> entry : bgCounts.entrySet())
		{
			sum += entry.getValue();
		}
		return sum;
	}
	
	public void incrementMotifCount(Character symbol, Double value)
	{
		double count = mCounts.get(symbol);
		mCounts.put(symbol, count + value);
	}
	
	public void incrementBGCount(Character symbol, Double value)
	{
		double count = bgCounts.get(symbol);
		bgCounts.put(symbol, count + value);
	}
	
	public Double getMotifCount(Character symbol)
	{
		return mCounts.get(symbol);
	}
	
	public Double getBGCount(Character symbol)
	{
		return bgCounts.get(symbol);
	}
	
	public Double getCountAtPosition(char symbol, int position)
	{
		return positionCounts.get(symbol)[position];
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		result += "Motif Counts:\n";
		for (Entry<Character, Double> entry : mCounts.entrySet())
		{
			result += entry.getKey() + " : " + entry.getValue() + "\n";
		}
		
		result += "\nBackground Counts:\n";
		for (Entry<Character, Double> entry : bgCounts.entrySet())
		{
			result += entry.getKey() + " : " + entry.getValue() + "\n";
		}
		
		return result;
	}

}
