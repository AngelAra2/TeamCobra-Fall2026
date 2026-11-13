package Model.Data;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;

import Model.AbstractClasses.Combatant;
import Model.Combat.StartingClass;
import Model.Exploration.Map;

public class DataFileExtraction 
{

	/* Extracts data from a file and stores it in the provided world map */
	public static void extractDataToMap(BiConsumer<Scanner, Map> dataParsingMethod, String filePath, Map worldMap)
	{
		/* Get specific data file */
		try (InputStream stream = Resources.getResourceAsStream(filePath))
		{
			// Try opening this file and extracting data from it 
			// using the provided data parsing method
			try
			{
				Scanner fileScanner = new Scanner(stream, StandardCharsets.UTF_8);
				dataParsingMethod.accept(fileScanner, worldMap);
				
				// Clean up
				fileScanner.close();
			}			
			
			finally {} // Left blank on purpose
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		finally {} // Left blank on purpose
	}

	/* Extracts states data from file and populates states machine in combatant */
	public static void extractStatesDataToCombatant(BiConsumer<Scanner, Combatant> dataParsingMethod, String filePath, Combatant combatant)
	{
		/* Get specific data file */
		try (InputStream stream = Resources.getResourceAsStream(filePath))
		{
			// Try opening this file and extracting data from it 
			// using the provided data parsing method
			try
			{
				Scanner fileScanner = new Scanner(stream, StandardCharsets.UTF_8);
				dataParsingMethod.accept(fileScanner, combatant);
				
				// Clean up
				fileScanner.close();
			}
			
			finally {} // Left blank on purpose
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		finally {} // Left blank on purpose
	}

	/* Extracts data from a file and stores it in the provided world map */
	public static void extractClassDataToController(BiConsumer<Scanner, ArrayList<StartingClass>> dataParsingMethod, String filePath, ArrayList<StartingClass> startingClasses)
	{
		/* Get specific data file */
		try (InputStream stream = Resources.getResourceAsStream(filePath))
		{
			// Try opening this file and extracting data from it
			// using the provided data parsing method
			try
			{
				Scanner fileScanner = new Scanner(stream, StandardCharsets.UTF_8);
				dataParsingMethod.accept(fileScanner, startingClasses);

				// Clean up
				fileScanner.close();
			}

			finally {} // Left blank on purpose
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}

		finally {} // Left blank on purpose
	}
	
	/* Extracts a line of data needed to create a game object */
	
	// Note: BiFunction takes in two arguments (Scanner/Integer) and returns a String
	public static String extractObjectData(BiFunction<Scanner, Integer, String> dataParsingMethod, String filePath, int id)
	{
		String objectData = "";
		
		/* Get specific data file */
		try (InputStream stream = Resources.getResourceAsStream(filePath))
		{
			// Try opening this file and extracting data from it
			// using the provided data parsing method
			try
			{
				Scanner fileScanner = new Scanner(stream, StandardCharsets.UTF_8);
				objectData = dataParsingMethod.apply(fileScanner, id);

				// Clean up
				fileScanner.close();
			}

			finally {} // Left blank on purpose
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}

		finally {} // Left blank on purpose
		
		return objectData;
	}
}

