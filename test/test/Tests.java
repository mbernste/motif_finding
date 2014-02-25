package test;

import meme.Counts;
import meme.Motif;
import meme.MotifLocations;
import meme.oops.MEME_OOPS;
import data.SequenceReader;
import data.SequenceSet;

public class Tests 
{
	public static void main(String[] args)
	{
		//testRandomMotif();
		//testSubSeqMotif();
		//testProbabilityOfSequence();
		//testEStep();
		//testCountSymbol();
		//testSymbolCountAtMotifPosition();
		//testTotalCountAtMotifPosition();
		//testMStep();
		//testCounts();
		//testPickoutMotif();
		testProbabilityOfData();
	}

	public static void testRandomMotif()
	{
		Motif motif = new Motif(3, MEME_OOPS.DNA_ALPHABET);
		System.out.println(motif);
	}
	
	public static SequenceSet getSequences()
	{
		SequenceSet sequences = SequenceReader.readFile("./data/dummy_sequences");
		return sequences;
	}
	
	public static Motif exampleMotif()
	{
		Motif motif = new Motif(3, "ACGT");
		
		motif.setProbability('A', 0, 0.25);
		motif.setProbability('C', 0, 0.25);
		motif.setProbability('G', 0, 0.25);
		motif.setProbability('T', 0, 0.25);
	
		motif.setProbability('A', 1, 0.1);
		motif.setProbability('C', 1, 0.4);
		motif.setProbability('G', 1, 0.3);
		motif.setProbability('T', 1, 0.2);
	
		motif.setProbability('A', 2, 0.5);
		motif.setProbability('C', 2, 0.2);
		motif.setProbability('G', 2, 0.1);
		motif.setProbability('T', 2, 0.2);
		
		motif.setProbability('A', 3, 0.2);
		motif.setProbability('C', 3, 0.1);
		motif.setProbability('G', 3, 0.6);
		motif.setProbability('T', 3, 0.1);
		
		System.out.println(motif);
		
		return motif;
	}
	
	public static void testProbabilityOfSequence()
	{
		Motif motif = exampleMotif();
		double probability = MEME_OOPS.probabilityOfSequence("GCTGTAG", motif, 2);
		System.out.println("Probability of sequence \"GCTGTAG\": " + probability);
	}
	
	public static void testEStep()
	{
		SequenceSet sequences = getSequences();
		MotifLocations z = MEME_OOPS.eStep(sequences, exampleMotif());
		
		System.out.println(z);
	}
	
	public static void testCountSymbol()
	{
		SequenceSet sequences = getSequences();
		System.out.println("Count of 'C': " + sequences.countSymbol('C'));
	}
	
	public static void testSymbolCountAtMotifPosition()
	{
		SequenceSet sequences = getSequences();
		MotifLocations z = MEME_OOPS.eStep(sequences, exampleMotif());
	
		System.out.println("Z");
		System.out.println(z);
		System.out.println("COUNT: " + sequences.countSymbolAtMotifPosition('C', 3, 3, z));
	}
	
	public static void testTotalCountAtMotifPosition()
	{
		SequenceSet sequences = getSequences();
		MotifLocations z = MEME_OOPS.eStep(sequences, exampleMotif());
		
		System.out.println("COUNT: " + sequences.totalCountAtMotifPosition('C', 2, 3, z));
	}
	
	public static void testMStep()
	{
		SequenceSet sequences = getSequences();
		
		Motif original = exampleMotif();
		
		MotifLocations z = MEME_OOPS.eStep(sequences, exampleMotif());
		
		Motif updated = MEME_OOPS.mStep(sequences, z, original.getWidth());
	
		System.out.println(z);
		System.out.println(updated);
	}
	
	public static void testCounts()
	{
		
		SequenceSet sequences = getSequences();
		Motif p = exampleMotif();
		MotifLocations z = MEME_OOPS.eStep(sequences, exampleMotif());
		
		Counts counts = new Counts(sequences, z, p);
		
		System.out.println("\nZ:\n" + z);
		System.out.println("\n" + counts);
	}
	
	public static void testPickoutMotif()
	{
		SequenceSet sequences = getSequences();
		Motif p = exampleMotif();
		MotifLocations z = MEME_OOPS.eStep(sequences, exampleMotif());
		
		System.out.println(sequences.pickOutMotifs(p, z));
	}
	
	public static void testSubSeqMotif()
	{
		String subSeq = "GTAT";
		Double param = 0.5;
		
		Motif p = new Motif(subSeq, MEME_OOPS.DNA_ALPHABET, param);
		
		System.out.println(p);
	}
	
	public static void testProbabilityOfData()
	{
		SequenceSet sequences = getSequences();
		Motif p = exampleMotif();
		MotifLocations z = MEME_OOPS.eStep(sequences, p);

		System.out.println(z);
		
		System.out.println("Probability of data: " + MEME_OOPS.probabilityOfData(sequences, p, z));
				
	}
}

