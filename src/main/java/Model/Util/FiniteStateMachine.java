package Model.Util;

import java.util.*;

public class FiniteStateMachine {
	/* Data Fields */
	private String currentState;
	private String previousState;
	private List<String> allStates;
	
	/* Constructors */
	public FiniteStateMachine(String[] allStates)
	{
		this.allStates = Arrays.asList(allStates);
		this.previousState = "";
		
		// Get the first state in the list and set it as the current state
		this.currentState = this.allStates.get(0);
	}

	/* Methods */

	// Getters //
	public String getCurrentState() { return this.currentState; }
	public String getPreviousState() { return this.previousState; }

	// Setters //
	public void setCurrentState(String newState)
	{
		// Change state only if the new state is in the list of all states and is not the same as the previous state
		if (this.allStates.contains(newState) && this.previousState != newState)
		{
			this.previousState = this.currentState;
			this.currentState = newState;
		}
		
		// New state is not in the list of all states
		else
			return;
	}
}

