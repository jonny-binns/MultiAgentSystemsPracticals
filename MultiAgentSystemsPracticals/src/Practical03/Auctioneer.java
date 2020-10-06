package Practical03;

import java.util.Hashtable;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class Auctioneer extends Agent {
	//catalogue of items to be sold
	private Hashtable catalogue;
	//list of bidderAgents
	private AID[] bidderAgents;
	
	
	//put agent initialisation here
	protected void setup() 
	{
		//Register the auctioneer with the DFAgent
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("auctioneer");
		sd.setName("JADE-auctioneer");
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
		
		//print out a welcome message
		System.out.println("Auctioneer " + getAID().getName() + " is ready");
		
		//create new catalouge
		catalogue = new Hashtable();
		//add test item to catalogue
		String prodName = "test";
		int startPrice = 10;
		addToCatalogue(prodName, startPrice);
		
		//add TickerBehaviour to look for bidders
		addBehaviour(new TickerBehaviour(this, 5000) {
			protected void onTick() {
				//update the list of bidderAgents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("bidder");
				template.addServices(sd);
				try
				{
					DFAgentDescription[] result = DFService.search(myAgent, template);
						bidderAgents = new AID[result.length];
					
					for (int i=0; i<result.length; ++i)
					{
						bidderAgents[i] = result[i].getName();
					}
					
				}
				catch (FIPAException fe)
				{
					fe.printStackTrace();
				}
			}
		});
		addBehaviour(new RequestPerformer());
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
		System.out.println("Auctioneer " + getAID().getName() + " is terminating");
	}
	
	//method to add product to catalogue
	public void addToCatalogue(final String title, final int startPrice) 
	{
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action () {
				catalogue.put(new String(title), new Integer(startPrice));
				System.out.println(catalogue.toString());
			}
		});
	}
	
	private class RequestPerformer extends Behaviour 
	{
		private MessageTemplate mt;
		private int step = 0;
		private AID bestBidder;
		private int bestPrice;
		private int repliesCnt = 0;
		
		public void action() 
		{
			switch(step)
			{
				case 0:
					//send the cfp to all bidders
					ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
					for(int i = 0; i < bidderAgents.length; ++i)
					{
						cfp.addReceiver(bidderAgents[i]);
					}
					cfp.setContent("test");
					cfp.setConversationId("auction");
					cfp.setReplyWith("cfp" + System.currentTimeMillis()); //unique value
					myAgent.send(cfp);
					//prepare the template to get proposals
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("auction"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
					step = 1;
					break;
				case 1:
					//recieve all proposals/refusals from seller agents
					ACLMessage reply = myAgent.receive(mt);
					if(reply != null)
					{
						//reply recieved
						if(reply.getPerformative() == ACLMessage.PROPOSE)
						{
							if (reply.getPerformative() == ACLMessage.PROPOSE)
							{
								//this is an offer
								int price = Integer.parseInt(reply.getContent());
								if(bestBidder == null || price > bestPrice)
								{
									//this is the best offer at present
									bestPrice = price;
									bestBidder = reply.getSender();
								}
							}
							repliesCnt++;
							if (repliesCnt >= bidderAgents.length)
							{
								//we have recieved all replies
								step = 2;
							}
						}
					}
					else
					{
						block();
					}
					break;
				case 2:
					//send the purchase order to the seller that provided the best offer
					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestBidder);
					order.setContent("test");
					order.setConversationId("auction");
					order.setReplyWith("order" + System.currentTimeMillis());
					myAgent.send(order);
					//prepare the template to get purchase order reply
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("auction"), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
					step = 3;
					break;
				case 3:
					//recieve the purchase order reply
					reply = myAgent.receive(mt);
					if (reply != null)
					{
						//Receive the purchase order reply 
						reply = myAgent.receive(mt);
						if (reply != null)
						{
							//purchase successful we can terminate
							System.out.println("Test" + " successfully purchased");
							System.out.println("Price = " + bestPrice);
							myAgent.doDelete();
						}
						step = 4;
					}
					else
					{
						block();
					}
					break;					
			}
		}
		
		public boolean done()
		{
			return ((step == 2 && bestBidder == null) || step == 4);
		}
	}
	
}
