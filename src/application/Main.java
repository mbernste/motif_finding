package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import oops.MEME_OOPS;

import meme.Motif;
import meme.MotifLocations;

import pair.Pair;
import data.SequenceReader;
import data.SequenceSet;

/**
 * The main program
 * 
 * @author matthewbernstein
 *
 */
public class Main 
{
	public static void main(String[] args)
	{
		/*
		 * Read arguments
		 */
		SequenceSet sequences = SequenceReader.readFile(args[0]);
		int width = Integer.parseInt(args[1]);
		File modelFile = new File(args[2]);
		File positionsFile = new File(args[3]);
		
		/*
		 * Run EM
		 */
		Pair<Motif, MotifLocations> result = MEME_OOPS.run(sequences, 14);
		Motif resultP = result.getFirst();
		MotifLocations resultZ = result.getSecond();
		ArrayList<Pair<Integer, String>> realizedMotifs 
									= sequences.pickOutMotifs(resultP, resultZ);
		
		/*
		 * Print results to console
		 */
		System.out.println("\n\nMotif:\n\n" + resultP);
		
		/*
		 * Write model and positions to file
		 */
		writeModelFile(modelFile, resultP);
		writePositionsFile(positionsFile, realizedMotifs);
		
		
		/*
		 * Print realized motifs
		 */
		System.out.print("\nRealized Motifs:\n\n");
		for (Pair<Integer, String> pair : realizedMotifs)
		{
			System.out.println(pair.getFirst() + "\t" + pair.getSecond());
		}
	}

	/**
	 * Write the positions of the motif in each sequence to the output file
	 * 
	 * @param positionsFile the file to be written
	 * @param realizedMotifs the location and realized motif in each sequence
	 */
	public static void writePositionsFile(File positionsFile, 
										  ArrayList<Pair<Integer, String>> realizedMotifs)
	{
		PrintWriter out = null;
		
		try 
		{
			out = new PrintWriter(positionsFile);
			
			for (Pair<Integer, String> pair : realizedMotifs)
			{
				out.write(pair.getFirst() + "\n");
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			out.close();
		}
		
	}
	
	/**
	 * Write the motif model matrix to the output file
	 * 
	 * @param modelFile the file to be written
	 * @param motif the motif model
	 */
	public static void writeModelFile(File modelFile, Motif motif)
	{
		PrintWriter out = null;

		try 
		{
			out = new PrintWriter(modelFile);

			out.write(motif.toString());

		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			out.close();
		}

	}
}
