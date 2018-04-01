
public class InnerNode implements Node {

	private String attr;
	private Node left;
	private Node right;

	public InnerNode(String attr, Node left, Node right) {
		this.attr = attr;
		this.left = left;
		this.right = right;
	}

	public void report(String indent) {
		System.out.format("%s%s = True:\n", indent, attr);
		left.report(indent + " ");
		System.out.format("%s%s = False:\n", indent, attr);
		right.report(indent + " ");
	}

	public String getAttr(){
		return attr;
	}
	
	public Node getLeft(){
		return left;
	}
	
	public Node getRight(){
		return right;
	}

}