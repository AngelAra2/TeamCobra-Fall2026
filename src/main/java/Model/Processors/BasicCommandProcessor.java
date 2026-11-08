package Model.Processors;

import Model.AbstractClasses.CommandProcessor;

public class BasicCommandProcessor extends CommandProcessor
{
	/* Constructors */
	public BasicCommandProcessor() { super(); }
	
	/* Methods */

	// Custom //	
	
	// Does absolutely nothing
	// This class is used to capture user input without trying to process it
	public void processCmd() {}
}

