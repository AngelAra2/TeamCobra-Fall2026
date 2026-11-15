package Model.Exploration;

import java.util.ArrayList;
import java.util.List;

import Model.Combat.Player;
import Model.Interfaces.Rewardable;

public class Puzzle
{
	/* Data Fields */
	private int puzzleId;
	private String puzzleName;
	private String puzzleDesc;
	private String puzzleSolution;
	private int maxAttempts;
	private int remainingAttempts;
	private boolean isSolved;
    private String hint1;
    private String hint2;
    private List<Rewardable> rewards = new ArrayList<>();
    private boolean rewardsGiven = false;

	/* Constructors */
	public Puzzle(
		int puzzleId,
		String puzzleName,
		String puzzleDesc,
		String puzzleSolution,
		int remainingAttempts,
		String hint1,
		String hint2
	)
	{
		
		this.puzzleId = puzzleId;
		this.puzzleName = puzzleName;
		this.puzzleDesc = puzzleDesc;
		this.puzzleSolution = puzzleSolution;
		this.maxAttempts = remainingAttempts;
		this.remainingAttempts = this.maxAttempts;
		this.isSolved = false;
		this.hint1 = hint1;
		this.hint2 = hint2;
	}

	/* Methods */
	
	// Private //
	private void decreaseRemainingAttempts()
	{
		if (this.remainingAttempts > 0)
			this.remainingAttempts -= 1;
	}

	// Getters //
	public int getPuzzleId() { return this.puzzleId; }
	public String getPuzzleName() { return this.puzzleName; }
	public String getPuzzleDescription() { return this.puzzleDesc; }
	public String getPuzzleSolution() { return this.puzzleSolution; }
	public int getMaxAttempts() { return this.maxAttempts; }
	public int getRemainingAttempts() { return this.remainingAttempts; }
	public boolean getIsSolved() { return this.isSolved; }

	// Setters //
	public void setIsSolved() { this.isSolved = true; }

	// Custom //
    public boolean checkSolution(String userInput)
    {
        // Do not check the solution if the puzzle is already solved
        if (this.isSolved)
            return true;

        // Correct solution
        if (userInput.equalsIgnoreCase(this.puzzleSolution))
        {
            this.setIsSolved();
            return true;
        }

        // Incorrect solution
        return false;
    }

    // Reset Puzzle State
	public void resetPuzzle()
	{
		this.isSolved = false;
		this.remainingAttempts = this.maxAttempts; // Reset to default number of attempts
	}

    public String getHint(int hintIndex)
    {
        switch (hintIndex)
        {
            case 0:
                return this.hint1;
            case 1:
                return this.hint2;
            default:
                throw new IndexOutOfBoundsException("Hint index must be 0 or 1");
        }
    }

    // Rewards //
    public void addReward(Rewardable reward) {
        this.rewards.add(reward);
    }

    public void giveRewards(Player player)
    {
        if (!this.isSolved || this.rewardsGiven)
            return;

        for (Rewardable reward : this.rewards)
            reward.give(player);

        this.rewardsGiven = true;
    }

	// Overrides //
	@Override
	public String toString()
	{
		return "[" + this.puzzleName + "] | "
				+ this.remainingAttempts + "/" 
				+ this.maxAttempts + " attempt(s) remaining\n" 
				+ "Puzzle prompt: " + this.puzzleDesc;
	}
}