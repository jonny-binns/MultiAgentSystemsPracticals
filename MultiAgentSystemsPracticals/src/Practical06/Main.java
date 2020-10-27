package Practical06;

import Practical05.SellerAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {

	public static void main(String[] args) {
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		
		try
		{
			ContainerController myContainer = myRuntime.createMainContainer(myProfile);
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			AgentController sellerAgent = myContainer.createNewAgent("seller", SellerAgent.class.getCanonicalName(),null);
			sellerAgent.start();
			
			AgentController cautiousBuyerAgent = myContainer.createNewAgent("cautiousBuyerAgent", CautiousBuyerAgent.class.getCanonicalName(),null);
			cautiousBuyerAgent.start();
			
			AgentController recklessBuyerAgent = myContainer.createNewAgent("recklessBuyerAgent", RecklessBuyerAgent.class.getCanonicalName(),null);
			recklessBuyerAgent.start();
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}

	}

}
