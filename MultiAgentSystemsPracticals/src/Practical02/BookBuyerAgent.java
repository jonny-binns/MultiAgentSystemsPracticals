package Practical02;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookBuyerAgent extends Agent {
	//the title of the book to buy
	private String targetBookTitle;
	//list of all known seller agents
	private AID[] sellerAgents;
	
	//put agent initializations here
	protected void setup() {
		//printout a welcome message
		System.out.println("Hello! Buyer-Agent " + getAID().getName() + " is ready");
		
		//Get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0)
		{
			targetBookTitle = (String) args[0];
			System.out.println("Trying to buy " + targetBookTitle);
			
			//add a TickerBehaviour that schedules a request to seller agents every 5 seconds
			addBehaviour(new TickerBehaviour(this, 5000) {
				protected void onTick() {
					//update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("book-selling");
					template.addServices(sd);
					try 
					{
						DFAgentDescription[] result = DFService.search(myAgent, template);
						sellerAgents = new AID[result.length];
						
						for (int i=0; i<result.length; ++i) 
						{
							sellerAgents[i] = result[i].getName();
						}
					}
					catch (FIPAException fe) 
					{
						fe.printStackTrace();
					}
					System.out.println("Checking...");
					//perform the request
					myAgent.addBehaviour(new RequestPerformer());
				}
			});
		}
		else
		{
			//make the agent terminate
			System.out.println("No book title specified");
			doDelete();
		}
	}
	
	//put agent clean up operations here
	protected void takedown() {
		//print a dismissal message
		System.out.println("Buyer-agent" + getAID().getName() + " terminating");
	}
	
	private class RequestPerformer extends Behaviour {
		private AID bestSeller; //the agent who provides the best offer
		private int bestPrice; //the best offered price
		private int repliesCnt = 0; //the counter of replies from seller agents
		private MessageTemplate mt; //the template to receive messages
		private int step = 0; 
		
		public void action () {
			switch (step) {
				case 0:
					//send the cfp to all sellers
					ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
					for (int i = 0; i < sellerAgents.length; ++i) {
						cfp.addReceiver(sellerAgents[i]);
					}
					cfp.setContent(targetBookTitle);
					cfp.setConversationId("book-trade");
					cfp.setReplyWith("cfp" + System.currentTimeMillis()); //unique value
					myAgent.send(cfp);
					//prepare the template to get proposals
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
					step = 1;
					break;
					
				case 1:
					//recieve all proposals/refusals from seller agents
					ACLMessage reply = myAgent.receive(mt);
					if (reply != null)
					{
						//Reply recieved
						if (reply.getPerformative() == ACLMessage.PROPOSE) 
						{
							//this is an offer
							int price = Integer.parseInt(reply.getContent());
							if(bestSeller == null || price < bestPrice) 
							{
								//this is the best offer at present
								bestPrice = price;
								bestSeller = reply.getSender();
							}
						}
						repliesCnt++;
						if (repliesCnt >= sellerAgents.length)
						{
							//we recieved all replies
							step = 2;
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
					order.addReceiver(bestSeller);
					order.setContent(targetBookTitle);
					order.setConversationId("book-trade");
					order.setReplyWith("order" + System.currentTimeMillis());
					myAgent.send(order);
					//prepare the template to get the puchase order reply
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
					step = 3;
					break;
				case 3:
					//recieve the purchase order reply
					reply = myAgent.receive(mt);
					if (reply != null)
					{
						//purchase order reply recieved
						if (reply.getPerformative() == ACLMessage.INFORM)
						{
							//purchase successful we can terminate
							System.out.println(targetBookTitle + " successfully puchased");
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
		
		public boolean done () 
		{
			return ((step == 2 && bestSeller == null ) || step == 4);
		}
	}

}
