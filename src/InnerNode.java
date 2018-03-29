
public class InnerNode implements Node{
	
	private String name;
	private double prob;
	
	public InnerNode(String name, double prob){
		this.name = name;
		this.prob = prob;
	}
	
	public String getName(){
		return name;
	}
	
	public double getProb(){
		return prob;
	}

}