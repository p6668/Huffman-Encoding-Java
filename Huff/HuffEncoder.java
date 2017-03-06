

/**
 * Implementation of the IHuffEncoder interface
 * This class is used to generate a table compression codings
 * from a huffman coding tree
 * @author Zifan Yang
 *
 */
public class HuffEncoder implements IHuffEncoder {
	private String[] table;
	/**
	 * Default constructor
	 * Initialize table to empty strings
	 */
	public HuffEncoder(){
		table = new String[IHuffHeader.ALPH_SIZE + 1]; // 1 extra for PSEUDO_EOF
		for (int i = 0; i < IHuffHeader.ALPH_SIZE + 1; i++){
			table[i] = "";
		}
	}
	 /**
     * Initialize state from a tree, the tree is obtained
     * from the treeMaker.
     * @param treeMaker used to generate tree for creating table/map.
     */
    public void makeTable(TreeMaker treeMaker){
        treeMaker.root = treeMaker.makeRoot();
    	makeCodings(treeMaker.root, "");
    }
    /**
     * Recursive helper function to make codings for each node
     */
    public void makeCodings(TreeNode root, String path){
    	if (root.isExternal()){
    			table[root.myValue] = path;
    		return;
    	}
    	makeCodings(root.myLeft, path + "0");
		makeCodings(root.myRight, path + "1");
    }
    
    /**
     * Returns coding, e.g., "010111" for specified chunk/character. It
     * is an errot to call this method before makeTable has been
     * called.
     * @param i is the chunk for which the coding is returned
     * @return the huff encoding for the specified chunk
     */
    public String getCode(int i){
    	return table[i];
    }
	
}
