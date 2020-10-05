package Practical03;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

/**
 * Buyer Changes
 * 		needs to randomly generate a price (from one to x) and send that to the seller
 * 		only needs to do this once
 * 		buyer should take price from argument
 * 
 * Auctioneer changes
 * 		add onTick to get list of buyers
 * 		request performer to get prices and figure out the highest one
 * 		request performer should print out winner
 * 
 * Application changed
 * 		start buyer agent
 * 		give buyer agent something to buy
 *
 */


public class Application {

	public static void main(String[] args) {
		//setup JADE environment
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		ContainerController myContainer = myRuntime.createMainContainer(myProfile);
		try
		{
			//agent controller
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//auctioneer agent
			String [] books = {"test", "10"};
			AgentController auctioneerAgent = myContainer.createNewAgent("auctioneer", Auctioneer.class.getCanonicalName(), books);
			auctioneerAgent.start();
			
			
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
		
	}

}
