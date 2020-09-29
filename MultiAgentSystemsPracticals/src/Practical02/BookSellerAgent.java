package Practical02;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.*;

public class BookSellerAgent extends Agent {
	//the catalogue of books for sale (maps the title of a book to its price)
	private Hashtable catalogue;
	//the GUI by means of which the user can add books in the catalogue
	private BookSellerGui myGui;
	
	
	//put agent initialisation here
	protected void setup () {
		//create the catalogue
		catalogue = new Hashtable();
		
		//create and show the GUI
		myGui = new BookSellerGui(this);
		myGui.showGui();
		
		//add the behaviour serving requests for offer from buyer agents
		addBehaviour(new OfferRequestsServer());
		
		//add the behaviour servering purchase orders from buyer agents
		addBehaviour(new PurchaseOrdersServer());
		
		//Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	
	//put agent clean up operations here
	protected void takeDown() {
		//Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		//close the GUI
		myGui.dispose();
		//printout a dismissal message
		System.out.println("Seller-agent " + getAID().getName() + " is terminating");
	}
	
	
	//this is invoked by the GUI when the user adds a new book for sale
	public void updateCatalogue(final String title, final int price) {
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action () {
				catalogue.put(title, new Integer(price));
				System.out.println(catalogue.toString());
			}
		});
	}
	
	
	private class OfferRequestsServer extends CyclicBehaviour {
		@Override
		public void action () {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			
			if (msg != null) {
				//CFP message received, process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				Integer price = (Integer) catalogue.get(title);
				if (price != null) 
				{
					//the requested book is available for sale, reply with price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				}
				else
				{
					//the requested book is not available for sale
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
	
	
	private class PurchaseOrdersServer extends CyclicBehaviour {
		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				//ACCEPT_PROPOSAL message received. process it
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
					//the requested book has been sold to another buyer in the meantime
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


