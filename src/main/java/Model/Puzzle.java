package Model;
public class Puzzle {
	/* Data Fields */
	private int puzzleId;
	private String puzzleName;
	private String puzzleDesc;
	private String puzzleSolution;
	private int maxAttempts;
	private int remainingAttempts;
	private boolean isSolved;

	/* Constructors */
	public Puzzle(
		int puzzleId,
		String puzzleName,
		String puzzleDesc,
		String puzzleSolution,
		int remainingAttempts
	)
	{
		
		this.puzzleId = puzzleId;
		this.puzzleName = puzzleName;
		this.puzzleDesc = puzzleDesc;
		this.puzzleSolution = puzzleSolution;
		this.maxAttempts = remainingAttempts;
		this.remainingAttempts = this.maxAttempts;
		this.isSolved = false;
	}

	/* Methods */
	
	// Private //
	private void decreaseRemainingAttempts()
	{
		if (this.remainingAttempts > 0)
			this.remainingAttempts -= 1;
	}

	// Getters //
	public int getPuzzleId()
	{
		return this.puzzleId;
	}
	
	public String getPuzzleName()
	{
		return this.puzzleName;
	}
	
	public String getPuzzleDescription()
	{
		return this.puzzleDesc;
	}
	
	public String getPuzzleSolution()
	{
		return this.puzzleSolution;
	}
	
	public int getMaxAttempts()
	{
		return this.maxAttempts;
	}
	
	public int getRemainingAttempts()
	{
		return this.remainingAttempts;
	}
	
	public boolean getIsSolved()
	{
		return this.isSolved;
	}

	// Setters //
	public void setIsSolved()
	{
		this.isSolved = true;
	}

	// Custom //
	public boolean checkSolution(String userInput)
	{
		/* Guard Clauses */
		// Do not check the solution if there are no remaining attempts
		if (this.remainingAttempts <= 0)
			return false;
		
		// Do not check the solution if the puzzle is already solved
		if (this.isSolved)
			return true;
		
		/* Compare user input to the puzzle solution (case-insensitive) */
		// Correct solution
		if (userInput.equalsIgnoreCase(this.puzzleSolution))
		{
			this.setIsSolved();
			return true;
		}
		
		// Incorrect solution
		else
		{
			this.decreaseRemainingAttempts();
			return false;
		}
	}
	
	public void resetPuzzle()
	{
		this.isSolved = false;
		this.remainingAttempts = this.maxAttempts; // Reset to default number of attempts
	}

	// Overrides //
	@Override
	public String toString()
	{
		return "[" + this.puzzleName + " - "
				+ this.remainingAttempts + "/" 
				+ this.maxAttempts + " attempts remaining]\n" 
				+ "- Puzzle prompt: " + this.puzzleDesc + "\n" 
				+ "- Your answer: ";
	}
}

