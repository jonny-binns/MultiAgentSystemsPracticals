package Chapter01;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class DemoAgent extends Agent {
	
	protected void setup () {
		addBehaviour(new B1(this));
		addBehaviour(new B2(this));
	}
	
	public class B1 extends Behaviour {
		private int timesCalled = 0;
		public B1(Agent a) {
			myAgent = a;
		}
		
		@Override
		public void action () {
			System.out.println(myAgent.getLocalName());
			timesCalled++;
		}
		
		@Override
		public boolean done() {
			return timesCalled >= 10;
		}
	}
	
	public class B2 extends Behaviour {
		private int timesCalled = 0;
		public B2(Agent a) {
			myAgent = a;
		}
		
		@Override
		public void action() {
			System.out.println(timesCalled);
			timesCalled++;
		}
		
		@Override
		public boolean done() {
			return timesCalled >= 20;
		}
	}

}