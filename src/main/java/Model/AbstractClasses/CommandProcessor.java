package Model.AbstractClasses;
import java.util.Scanner;

public abstract class CommandProcessor
{
	/* Data Fields */
	protected String currentCmd;
	protected String cmdType;
	protected String processedCmd;
	protected Scanner inputSC;

	/* Constructors */
	public CommandProcessor()
	{
		this.currentCmd = "";
		this.inputSC = new Scanner(System.in);
	}
	
	/* Methods */
	
	// Private //
	
	// Getters //
	public String getCommandType() { return this.cmdType; }
	public String getProcessedCommand() { return this.processedCmd; }
	
	// Setters //
	public void setCurrentCommand(String newCmd)
	{
		this.currentCmd = newCmd;
		this.processCmd();
	}
	
	// Custom //
	
	// Command processor subclasses will implement this method to process the current command
	public abstract void processCmd();

	// Yields current thread until the user types something in and presses [Enter]/[Return]
	public String getUserInput() { return inputSC.nextLine().trim(); }
	
	// Cleans up this class object
	public void destroy() { this.inputSC.close(); }
	
	public void reset()
	{
		// Sets strings to empty strings
		this.cmdType = "";
		this.processedCmd = "";
	}
}

