
/**
 * The class that implements the ITreeMaker interface
 * The class stores a forest of treenodes in a priority queue
 * When makeRoot() is called, the tree is constructed using the priority queue by 
 * removing two minimum node and creating a parent until the root node is returned
 * @author Zifan Yang
 *
 */
public class TreeMaker implements ITreeMaker {
	private TreeNodePriorityQueue treeNodes; 
	public TreeNode root;
	public TreeMaker(){
		treeNodes = new TreeNodePriorityQueue();
	}
	public int size(){
		return treeNodes.size();
	}
	@Override
	public TreeNode makeRoot() {
		TreeNode root; 
    	if(treeNodes.size() == 1){
    		return treeNodes.removeMin(); 
    	}
    	
    	TreeNode left = treeNodes.removeMin(); 
    	TreeNode right = treeNodes.removeMin(); 
    	treeNodes.insert(new TreeNode(-1, 
    				left.myWeight + right.myWeight, left, right)); 
    	root = makeRoot(); 
    	
    	return root; 
	}
	public void addNode(int value, int weight){
		TreeNode newNode = new TreeNode(value, weight);
		treeNodes.insert(newNode);
		
	}
}
