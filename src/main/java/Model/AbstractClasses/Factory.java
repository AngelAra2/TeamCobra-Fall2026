package Model.AbstractClasses;

import Model.Exploration.Map;

/*
 * Factory is an abstract class that serves as a blueprint for creating specific factory classes in the application.
 * This class can be extended by other factory classes to create instances of various objects, such as monsters, 
 * items, or other game objects
 */

public abstract class Factory<T>
{
	/* Data Fields */
	protected String filePath; // Path to the data file used for creating objects
	protected Map worldMap; // Reference to the world map, which may be used for object creation

	/* Constructors */
	public Factory(String filePath, Map worldMap) 
	{
		this.filePath = filePath;
		this.worldMap = worldMap;
	}

	/* Methods */

	// Custom //
	public abstract T create(int id);
	
}

