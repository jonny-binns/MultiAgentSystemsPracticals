package Practical03;

import java.util.Hashtable;
import java.util.Random;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Bidder extends Agent {
	//name of product to be bid on
	//private String productName;
	//bid for item
	//private int bid;

	
	//put agent initalizations here
	protected void setup() 
	{
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
		
		//set item to be bid on & bid amount
		String productName = "test";
		
		//create random bid
		Random rand = new Random();
		int randBid = rand.nextInt(20);
		
		//printout a welcome message
		System.out.println("Bidder: " + getAID().getName() + " is ready with bid " + randBid);
		
		
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
	
}
