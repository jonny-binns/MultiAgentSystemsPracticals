package Practical06;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class ECommerseOntology extends BeanOntology {
	
	private static Ontology theInstance = new ECommerseOntology("my_ontology");
	
	public static Ontology getInstance()
	{
		return theInstance;
	}
	
	//singleton pattern
	private ECommerseOntology(String name)
	{
		super(name);
		try
		{
			add("Practical06.elements");
		}
		catch (BeanOntologyException e)
		{
			e.printStackTrace();
		}
	}

}
