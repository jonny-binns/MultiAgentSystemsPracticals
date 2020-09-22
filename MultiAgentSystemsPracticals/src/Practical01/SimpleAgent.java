package Practical01;
import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

public class SimpleAgent extends Agent {
		/*
		//This method is called when the agent is launched
		protected void setup() {
			//print out a welcome message
			System.out.println("Hello Agent"+getAID().getName()+" is ready.");
		}
		 */
	
		/*
		//code to print out its name every 10 seconds
		//get time agent was launched
		long t0 = System.currentTimeMillis();
	
		Behaviour loop;
		protected void setup()
		{
			loop = new TickerBehaviour(this, 10000)
			{
				protected void onTick()
				{
					System.out.println(getAID().getName());
				}
			};
			addBehaviour(loop);	
		}
		*/
	
		//get the current time - this will be the time that the agent was launched at
		long t0 = System.currentTimeMillis();
		Random rand = new Random();
		int randInt = rand.nextInt(30000); 
		
		
		Behaviour loop;
		protected void setup()
		{
			loop = new TickerBehaviour(this, 300)
			{
				protected void onTick()
				{
					if(System.currentTimeMillis() - t0 > randInt)
					{
						System.out.println("Agent deleted itself after " + randInt + " ms");
						myAgent.doDelete();
					}
				}
			};
			addBehaviour(loop);
		}

}