package Model.Data;
import java.io.*;
import java.util.*;
import java.util.function.*;

import Model.AbstractClasses.Combatant;
import Model.Exploration.Map;

public class DataFileExtraction 
{

	/* Data Fields */
	// Construct base directory path for data files
	private static File projectDir = new File(System.getProperty("user.dir"));
		private static File srcDir = new File(projectDir, "src");
			private static File resourcesDir = new File(srcDir, "resources");
	
	/* Extracts data from a file and stores it in the provided world map */
	public static void extractDataToMap(BiConsumer<Scanner, Map> dataParsingMethod, String filePath, Map worldMap)
	{
		/* Get specific data file */
		File dataFile = new File(resourcesDir, filePath);
		
		// Try opening this file and extracting data from it 
		// using the provided data parsing method
		try
		{
			Scanner fileScanner = new Scanner(dataFile);
			dataParsingMethod.accept(fileScanner, worldMap);
			
			// Clean up
			fileScanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally {} // Left blank on purpose
	}

	/* Extracts states data from file and populates states machine in combatant */
	public static void extractStatesDataToCombatant(BiConsumer<Scanner, Combatant> dataParsingMethod, String filePath, Combatant combatant)
	{
		/* Get specific data file */
		File dataFile = new File(resourcesDir, filePath);
		
		// Try opening this file and extracting data from it 
		// using the provided data parsing method
		try
		{
			Scanner fileScanner = new Scanner(dataFile);
			dataParsingMethod.accept(fileScanner, combatant);
			
			// Clean up
			fileScanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally {} // Left blank on purpose
	}
}

