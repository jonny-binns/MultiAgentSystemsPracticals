package Practical02;
import jade.core.Agent;
import jade.core.behaviours.*;
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
		myGui.show();
		
		//add the behaviour serving requests for offer from buyer agents
		addBehaviour(new OfferRequestsServer());
		
		//add the behaviour servering purchase orders from buyer agents
		addBehaviour(new PurchaseOrdersServer());
	}
	
	//put agent clean up operations here
	protected void takeDown() {
		//close the GUI
		myGui.dispose();
		//printout a dismissal message
		System.out.println("Seller-agent " + getAID().getName() + " is terminating");
	}
	
	//this is invoked by the GUI when the user adds a new book for sale
	public void updateCatalogue(final String title, final int price) {
		addBehaviour(new OneShotBehaviour() {
			public void action () {
				catalogue.put(title, new Integer(price));
			}
		});
	}
	
	private class OfferRequestsServer extends CyclicBehaviour {
		public void action () {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				//message received, process it
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
		}
	}
	
	private class PurchaseOrdersServer extends CyclicBehaviour {
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
					System.out.println(title + " sold to agent " + msg.getSender().getName());
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


