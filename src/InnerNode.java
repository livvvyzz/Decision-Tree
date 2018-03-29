
public class InnerNode implements Node{
	
	private String attr;
	private Node left;
	private Node right;
	
	public InnerNode(String attr, Node left, Node right){
		this.attr = attr;
		this.left = left;
		this.right = right;
	}
	

}