package Chapter01;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;

public class SequentialAgent extends Agent {
	private int result = 0;
	
	@Override
	protected void setup() {
		SequentialBehaviour s1 = new SequentialBehaviour(this);
		s1.addSubBehaviour(new B1());
		s1.addSubBehaviour(new B2());
		s1.addSubBehaviour(new B3());
		addBehaviour(s1);
	}
	
	public class B1 extends OneShotBehaviour {
		@Override
		public void action() {
			result += 10;
			System.out.println(result);
		}
	}
	
	public class B2 extends OneShotBehaviour {
		@Override
		public void action() {
			result += 100;
			System.out.println(result);
		}
	}
	
	public class B3 extends OneShotBehaviour {
		@Override
		public void action() {
			result += 200;
			System.out.println(result);
		}
	}
}