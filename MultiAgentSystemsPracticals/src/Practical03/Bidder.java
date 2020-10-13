package Practical03;

import java.util.Hashtable;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Bidder extends Agent {
	//name of product to be bid on
	private String productName;
	//bid for item
	private int bid;

	
	//put agent initalizations here
	protected void setup() 
	{
		//set item to be bid on & bid amount
		String productName = "test";
		
		//create random bid
		Random rand = new Random();
		int randBid = rand.nextInt(20);
		
		
		//Register the auctioneer with the DFAgent
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("bidder");
		sd.setName("JADE-bidder");
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}	
		
		
		//printout a welcome message
		System.out.println("Bidder: " + getAID().getName() + " is ready with bid " + randBid);
		
		//behaviour to recieve a CFP from the autioneer
		addBehaviour(new ReceiveCFP(this));
		
		//behaviour to deal with winning an auction
		addBehaviour(new Winner(this));
		
		
	}
	
	//clean up operations
	protected void takeDown()
	{
		//deregister from DFAgent
		try 
		{
			DFService.deregister(this);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
		
		//print dismissal message
		System.out.println("Bidder " + getAID().getName() + " is terminating");
	}
	
	/*
	 * modified from https://github.com/ardiyu07/jade-blind-auction/blob/master/src/blindAuction/BidderHuman.java
	 */
	
	private class ReceiveCFP extends CyclicBehaviour {
		private Bidder myAgent;
		private ReceiveCFP(Bidder agent)
		{
			super(agent);
			myAgent = agent;
		}
		
		@Override
		public void action()
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg != null)
			{
				//CFP received
				String answer = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				if(answer.equals(productName))
				{
					int bidPrice = bid;
					
					System.out.print(myAgent.getLocalName() + ": Auction commenced, current item is: " + answer + " bid: " + bidPrice);
					
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(Integer.toString(bid));
				}
				else
				{
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else
			{
				block();
			}
		}
	}
	
	//deal with being the winner of an auction
	private class Winner extends CyclicBehaviour{
		private Bidder myAgent;
		
		private Winner(Bidder agent)
		{
			super(agent);
			myAgent = agent;
		}
		
		@Override
		public void action() 
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg != null) 
			{
				//ACCEPT_PROPOSAL received. process it
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				myAgent.send(reply);
				System.out.println(myAgent.getLocalName() + " won");
			}
			else
			{
				block();
			}
		}
	}
	
}
