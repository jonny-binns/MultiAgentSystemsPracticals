package Practical02;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Application {

	public static void main(String[] args) {
		//setup the JADE environment
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance(); 
		ContainerController myContainer = myRuntime.createMainContainer(myProfile);
		try 
		{
			//start the agent controller
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//now start our own BookBuyerAgent, called buyer
			AgentController myAgent = myContainer.createNewAgent("Buyer", BookBuyerAgent.class.getCanonicalName(), null);
			myAgent.start();	
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
		
	}

}
