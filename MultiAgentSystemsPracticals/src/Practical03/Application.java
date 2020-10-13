package Practical03;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Application {

	public static void main(String[] args) { 
		//setup JADE enviroment 
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		ContainerController myContainer = myRuntime.createMainContainer(myProfile);
		
		try
		{
			//start agent controller
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//start an auctioneer agent
			//AgentController auctioneerAgent = myContainer.createNewAgent("auctioneer", Auctioneer.class.getCanonicalName(), null);
			//auctioneerAgent.start();
			
			//start a bidder agent
			AgentController bidderAgent = myContainer.createNewAgent("bidder", Bidder.class.getCanonicalName(), null);
			bidderAgent.start();
			
			//start a bidder agent
			//AgentController bidderAgent2 = myContainer.createNewAgent("bidder2", Bidder.class.getCanonicalName(), null);
			//bidderAgent2.start();
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
	}

}
