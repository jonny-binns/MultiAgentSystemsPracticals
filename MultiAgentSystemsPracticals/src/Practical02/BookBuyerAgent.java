package Practical02;
import jade.core.AID;
import jade.core.Agent;

public class BookBuyerAgent extends Agent {
	protected void setup() {
		//print a welcome message
		System.out.println("Hello Buyer-agent " + getAID().getName() + " is ready");
		String nickname = "Peter";
		AID id = new AID(nickname, AID.ISLOCALNAME);
	}

}
