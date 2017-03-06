import java.io.IOException;
/**
 * The class that implements the IHuffDecoder interface
 * The class is used to read a compressed and uncompress it using a coding tree
 * The coding tree information is extracted by HuffHeader's readHeader method
 * @author Zifan Yang
 *
 */

public class HuffDecoder implements IHuffDecoder {
	private TreeNode root;
	/**
     * Initialize by having access to the tree supplied by the treeMaker
     * parameter so that subsequent calls to <code>doDecode</code> will be
     * able to read one bit-at-a-time and write values encoded in the input
     * file by accessing leaves of the tree appropriately. Presumably the tree
     * is stored in some state accessible to <code>doDecode</code>.
     * @param treeMaker is the source of the tree that will be used to decode
     * a compressed file
     */
	@Override
	public void initialize(TreeMaker treeMaker) {
		root = treeMaker.root;
	}
	 /**
     * Read one bit-at-a-time from the input file which is presumed to be a file
     * compressed by this suite of classes. The data stored in the file is written
     * to the output file one char/int/word at a time, e.g., when a leaf of the
     * tree used to initialize this object is reached. The output stops when
     * PSEUDO_EOF is reached (or bits run out)
     * @param input is source of compressed bits, the header has been read already
     * @param output is where uncompressed file is written, BITS_PER_WORD at-a-time
     * assuming that's what is stored in leaves of the tree used for initialization
     * @throws IOException if bits run out before reaching PSEUDO_EOF or other
     * I/O error occurs
     */

	@Override
	public void doDecode(BitInputStream input, BitOutputStream output) throws IOException {
		int bits = 0;
		TreeNode walk = root;
		while (true){
	        	bits = input.read(1); 
	            if (bits == -1){
	               throw new IOException("Should not happen! Trouble reading bits!"); 
	            }
	            else{ 
	            	// use the zero/one value of the bit read
	            	// to traverse Huffman coding tree
	            	// if a leaf is reached, decode the character and print UNLESS
	            	// the character is pseudo-EOF, then decompression done

	                if ( (bits & 1) == 0){ walk = walk.myLeft; }// read a 0, go left in tree
	                else{ walk = walk.myRight;}  //  read a 1, go right in tree               

	                if (walk.isExternal()){
	                	if (walk.myValue == PSEUDO_EOF){
	                    	break; 
	                    }
	                	else{
	                    	output.write(BITS_PER_WORD, walk.myValue);
	                    	walk = root; 
	                	}
	                }              	
	            } 
	    }
		input.close();
		output.close();
		
	}

}
