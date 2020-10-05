package Practical03;

import java.util.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class Auctioneer extends Agent {
	//catalogue of things for sale
	private Hashtable catalogue;
	
	//Agent initialisation
	protected void setup() 
	{
		//create the new catalogue
		catalogue = new Hashtable();
		
		//add product to catalogue
		Object[] args = getArguments();
		String title = (String) args[0];
		updateCatalogue(title);
		
		//add behaviour serving bids from bidders
		addBehaviour(new OfferRequestsServer());
		
		//add behaviour for serving puchase orders
		addBehaviour(new PurchaseOrdersServer());
		
		//register the auctioneer in the DF Agent
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
		System.out.println("Auctioneer " + getAID().getName() + " has started");
	}
	
	
	//agent clean up operations
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
		
		//print out a dismissal message 
		System.out.println("Auctioneer " + getAID().getName() + " is terminating");
	}
	
	//update catalogue method
	public void updateCatalogue(final String title)
	{
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() 
			{
				catalogue.put(title, new Integer(0));
				System.out.println("Catalogue Updated:");
				System.out.println(catalogue.toString());
			}
		});
	}
	
	
	//offer requests server
	private class OfferRequestsServer extends CyclicBehaviour
	{
		@Override
		public void action()
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg != null)
			{
				//cfp message received
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				//this will need to be changed to compare bids
				Integer price = (Integer) catalogue.get(title);
				if(price != null)
				{
					//the requested item is available for sale, reply with price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				}
				else
				{
					//the requested item is not available for sale
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
	
	//purchase orders server
	private class PurchaseOrdersServer extends CyclicBehaviour
	{
		@Override
		public void action() 
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			
			if (msg != null)
			{
				//accept_proposal message recieved
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				Integer price = (Integer) catalogue.remove(title);
				if (price != null)
				{
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title + " sold to agent " + msg.getSender().getName() + " for " + price);
				}
				else
				{
					//the requested item has been sold to another buyer in the meantime
					reply.setPerformative(ACLMessage.FAILURE);
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
	
	
}
