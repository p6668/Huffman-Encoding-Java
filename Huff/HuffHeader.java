import java.io.IOException;
 /**
  * The class that implements the IHuffHeader interfacce
  * This class is used for writing a header to a new compressed file or
  * reading a header from a compressed file to extract header information
  * The header contains the information needed for uncompression including magic number and the coding tree
  * @author Zifan Yang
  *
  */
public class HuffHeader implements IHuffHeader{
	private int size;
	
	CharCounter count;
	/**
	 * Default constructor initialize the header size to 0
	 */
	public HuffHeader(){
		size = 0;
	}
	/**
	 * Used when compressing a file
	 * @param root  the root of the encoding tree used to write the tree into the header
	 */
	public HuffHeader(CharCounter count){
		size = 0;
		this.count = count;
	}

	/**
     * The number of bits in the header using the implementation, including
     * the magic number presumably stored.
     * @return the number of bits in the header
     */
	@Override
	public int headerSize() {
		return size;
	}
	/**
     * Write the header, including magic number and all bits needed to
     * reconstruct a tree, e.g., using <code>readHeader</code>.
     * @param out is where the header is written
     */
	@Override
	public void writeHeader(TreeNode root, BitOutputStream out) {
		out.write(BITS_PER_INT, MAGIC_NUMBER);
		size += BITS_PER_INT;
		writeHelper(root, out);
	}
	
	/**
     * Read the header and return an ITreeMaker object corresponding to
     * the information/header read.
     * @param in is source of bits for header
     * @return an ITreeMaker object representing the tree stored in the header
     * @throws IOException if the header is bad, e.g., wrong MAGIC_NUMBER, wrong
     * number of bits, I/O error occurs reading
     */    
	@Override
    public void writeHelper(TreeNode node, BitOutputStream out) {
        // TODO Auto-generated method stub
	    if(node.isExternal()){
	        out.write(1, 1);
	        out.write(9, node.myValue);
	        return;
	    }
	    out.write(1, 0);
	    writeHelper(node.myLeft, out);
	    writeHelper(node.myRight, out);
        
    }
	@Override
	public TreeNode readHeader(BitInputStream in) throws IOException {
		int check = in.read(BITS_PER_INT);
		if (check != MAGIC_NUMBER){
			throw new IOException("Magic number incorrect!");
		}
		return readHelper(in);
		
	}
    @Override
    public TreeNode readHelper(BitInputStream in) throws IOException {
        // TODO Auto-generated method stub
        int flag = in.read(1);
        if(flag == 1)
            return(new TreeNode(in.read(9), 0, null, null));
        else
            return(new TreeNode(0, 0, readHelper(in), readHelper(in)));
        
    }
 

}
