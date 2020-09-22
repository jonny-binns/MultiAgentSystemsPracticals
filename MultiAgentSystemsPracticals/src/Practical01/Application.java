package Practical01;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;

public class Application {
	public static void main(String[] args) {
		//setup the jade enviroment
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		ContainerController myContainer = myRuntime.createMainContainer(myProfile);
		
		/*
		// code to get SimpleAgent running
		try {
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//Now start our own SimpleAgent, called Fred
			AgentController myAgent = myContainer.createNewAgent("Fred", SimpleAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch(Exception e) {
			System.out.println("Exception starting agent " + e.toString());
		}
		*/
		
		/*
		//code to get TimerAgent running
		try {
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//Now start our own TimerAgent, named Time
			AgentController myAgent = myContainer.createNewAgent("Time", TimerAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch(Exception e) {
			System.out.println("Exception starting agent " + e.toString());
		}
		*/
		
		/*
		//code for TickerAgent
		try {
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//Now start our own TickerAgent, named Tick
			AgentController myAgent = myContainer.createNewAgent("Tick", TickerAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch(Exception e) 
		{
			System.out.println("Exception starting agent " + e.toString());
		}
		/*
		
		/*
		//code to launch 10 SimpleAgents
		try {
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//loop to launch 10 SimpleAgents
			for (int i=0; i<10; i++)
			{
				//create name for each simpleAgent
				String name = "SimpleAgent" + i;
				
				//Now start our own SimpleAgent, called i+SimpleAgent
				AgentController myAgent = myContainer.createNewAgent(name, SimpleAgent.class.getCanonicalName(), null);
				myAgent.start();
			}
		}
		catch(Exception e) 
		{
			System.out.println("Exception starting agent " + e.toString());
		}
		*/

	}

}