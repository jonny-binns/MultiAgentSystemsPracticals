package Chapter01;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;

public class Application {

	public static void main(String[] args) {
		//Setup the JADE environment
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		ContainerController myContainer = myRuntime.createMainContainer(myProfile);
		
		/*
		//code to setup SimpleAgent
		try
		{
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//now start our simple agent, named fred
			AgentController myAgent = myContainer.createNewAgent("Fred", SimpleAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
		*/
		
		/*
		//code to setup TimerAgent
		try
		{
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//now start our simple agent, named fred
			AgentController myAgent = myContainer.createNewAgent("Timer", TimerAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
		*/
		
		/*
		//code to run DemoAgent
		try
		{
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			//now start our simple agent, named fred
			AgentController myAgent = myContainer.createNewAgent("Demo", DemoAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
		*/
		
		/*
		//code to run SequentialAgent
		try
		{
			//start the agent controller, which is itself an agent (rma)
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
					
			//now start our simple agent, named fred
			AgentController myAgent = myContainer.createNewAgent("Sequential", SequentialAgent.class.getCanonicalName(), null);
			myAgent.start();
		}
		catch (Exception e)
		{
			System.out.println("Exception starting agent: " + e.toString());
		}
		*/
		
	}

}