package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SequenceReader 
{

	public static SequenceSet readFile(String file) 
	{
		SequenceSet sequences = new SequenceSet();

		try
		{
			Scanner scan = new Scanner(new FileInputStream(file));

			/*
			 *	Process every line in the file
			 */
			String line = null;
			while (scan.hasNextLine()) 
			{
				sequences.addSequence( scan.nextLine() );
			}

			scan.close();	    
		} 
		catch (FileNotFoundException x) 
		{
			System.err.format("FileNotFountException: %s%n", x);
		}

		return sequences;
	}
}
