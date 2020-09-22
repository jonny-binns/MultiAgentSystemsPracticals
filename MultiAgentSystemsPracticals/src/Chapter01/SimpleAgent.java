package Chapter01;
import jade.core.Agent;

public class SimpleAgent extends Agent {
	//This method is called when the agent is launched
	protected void setup() {	
		//print out a welcome message
		System.out.println("Hello! Agent " + getAID().getName() + " is ready.");
	}
	
}