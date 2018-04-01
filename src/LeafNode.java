
public class LeafNode implements Node {

	private String name;
	private double prob;

	public LeafNode(String name, double prob) {
		this.name = name;
		this.prob = prob;
	}

	public String getName() {
		return name;
	}

	public double getProb() {
		return prob;
	}

	public void report(String indent) {
		System.out.format("%sClass %s, prob=%s\n", indent, name, prob);
	}


}
