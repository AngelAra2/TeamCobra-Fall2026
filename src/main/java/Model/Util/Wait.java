package Model.Util;

public class Wait {

	public static void yield(float seconds) {
		try 
		{
			// Convert seconds to milliseconds
			Thread.sleep( (int)(seconds * 1000) );
		} 
		catch (InterruptedException e) 
		{
			// Restore interrupted status
			Thread.currentThread().interrupt();
		}
	}
	
}

