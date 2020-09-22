package Practical01;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

public class TickerAgent extends Agent{
	//get the current time - this will be the time that the agent was launched at
	long t0 = System.currentTimeMillis();
	
	Behaviour loop;
	protected void setup()
	{
		loop = new TickerBehaviour(this, 300)
		{
			protected void onTick()
			{
				if(System.currentTimeMillis() - t0 < 60000)
				{
					//print elapsed time since launch
					System.out.println(System.currentTimeMillis() - t0 + ":" + myAgent.getLocalName());
				}
				else 
				{
					System.out.println("1 Minute has passed");
					myAgent.doDelete();
				}
			}
		};
		addBehaviour(loop);
	}
}