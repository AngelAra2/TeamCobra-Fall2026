package Model.Data;

import java.io.*;

public class Resources 
{

	/* Gets the InputStream for a resource file */
	public static InputStream getResourceAsStream(String resourcePath) throws IOException
	{
		//InputStream stream = Resources.class.getResourceAsStream(resourcePath);
		InputStream stream = Resources.class.getResourceAsStream(resourcePath);
		
		// Throw exception if the resource file is not found
		if (stream == null)
			throw new IOException("Resource file not found: " + resourcePath);

		return stream;
	}
	
}

