package Chapter01;
import java.util.ArrayList;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class TimerAgent extends Agent {
	/*
	int w = 15;
	//code to count down from 15 seconds
	public void setup()
	{
		//create a new tickerBehaviour
		addBehaviour(new TickerBehaviour(this, 1000) {
			//call onTick every 1000 ms
			protected void onTick() 
			{
				if (w > 0)
				{
					System.out.println(w + " Seconds left");
					w--;
				}
				else
				{
					System.out.println("Goodbye");
					//delete agent
					myAgent.doDelete();
				}
			}
		});
	}
	*/
	
	//code to search for a new SimpleAgent once every 60 seconds
	private ArrayList<AID> simpleAgents = new ArrayList<>();
	
	protected void setup() 
	{
		addBehaviour(new TickerBehaviour(this, 60000) {
			protected void onTick() 
			{
				//create a template for the agent service we are looking for 
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Simple-Agent");
				template.addServices(sd);
				
				//query the DF agent
				try
				{
					DFAgentDescription[] result = DFService.search(myAgent, template);
					simpleAgents.clear(); //we're going to replace this
					for (int i=0; i<result.length; i++)
					{
						simpleAgents.add(result[i].getName()); //this is the AID
					}
				}
				catch (FIPAException e)
				{
					e.printStackTrace();
				}
			}
			
		});
	}

}