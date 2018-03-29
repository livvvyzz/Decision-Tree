
public class LeafNode implements Node{
	
	private String name;
	private double prob;
	
	public LeafNode(String name, double prob){
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
