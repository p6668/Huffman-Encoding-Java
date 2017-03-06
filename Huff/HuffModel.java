import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
/**
 * The class that implements the IHuffModel interface
 * All HuffViewer selections compress/uncompress/show char/codings are handled by this class.
 * @author Zifan Yang
 *
 */
public class HuffModel implements IHuffModel {
	private CharCounter count; // counter for characters 
	private ArrayList<String> countList; // list of character counts
	private ArrayList<String> codingList;// list of codings
	private TreeMaker tree; // Tree maker for creating the huffman tree
    private HuffViewer view; // The user interface
    private HuffHeader header;// Handles writing/reading header to/from a file
    private HuffEncoder encoder; // Used to create the coding table
    private HuffDecoder decoder; // used to uncompress information after the header
    private int initialSize; // The initial size of the file
	HuffModel(){
		count = new CharCounter();
		countList = new ArrayList<String>();
		codingList = new ArrayList<String>();
		tree = new TreeMaker();
		encoder = new HuffEncoder();
		decoder = new HuffDecoder();
		header = new HuffHeader(count);
	}
    /**
     * Display all encodings (via the associated view).
     */
    public void showCodings(){
    	codingList.clear();
    	for (int i = 0; i < IHuffHeader.ALPH_SIZE; i++){
    		if (encoder.getCode(i) != "")// Show codings only for character appeared at least once
    			codingList.add(i + " " + encoder.getCode(i) );
    	}
    	view.update(codingList);
    }
    
    /**
     * Display all chunk/character counts (via the associated view).
     */
    public void showCounts(){
    	countList.clear();
    	for (int i = 0; i < IHuffHeader.ALPH_SIZE; i++){
    		if (count.getCount(i) > 0)// Show counts only for character appeared at least once
    			countList.add(i + " " + count.getCount(i)); 
    	}
    	view.update(countList);
    	
    }
    
    /**
     * Initialize state via an input stream. The stream most
     * likely comes from a view, it's NOT a BitInputStream
     * @param stream is an input stream for initializing state of this model
     * @throws IOException if the reading fails
     */
    public void initialize(InputStream stream) throws IOException{
    	initialSize = count.countAll(stream);// Get the initial size for comparison later
    	for (int i = 0; i < IHuffHeader.ALPH_SIZE; i++){
    		if (count.getCount(i) > 0)// only include characters appeared at least once
    			tree.addNode(i, count.getCount(i));
    
    	}
    	tree.addNode(PSEUDO_EOF, 1);// add PSEUDO_EOF node
    	
    	encoder.makeTable(tree);// make the table needed for displaying and compressing
    	
    }
    
    /**
     * Write a compressed version of the data read
     * by the InputStream parameter, -- if the stream is
     * not the same as the stream last passed
     * to initialize, then compression won't be optimal, but will still
     * work. If force is false, compression only occurs if it saves
     * space. If force is true compression results even if no bits are saved.
     * @param stream is the input stream to be compressed
     * @param file specifes the file to be written with compressed data
     * @param force indicates if compression forced
     * @throws IOException if the I/O fails or force compression is required
     */
    public void write(InputStream stream, File file, boolean force) throws IOException{
    	int beforeSize = initialSize;
    	int afterSize = 0;
    	int sizeChange = 0;
    	int bits = 0;
    	String charCoding = "";
    	BitOutputStream out = new BitOutputStream(file.getCanonicalPath());
    	// write header
    	header.writeHeader(tree.root, out);
    	afterSize += header.headerSize();
    	
    	// Compress using the encoder table
    	while((bits = stream.read()) != -1){
    		charCoding = encoder.getCode(bits);
    		for (int i = 0; i < charCoding.length(); i++){
    			if (charCoding.charAt(i) == '0'){
    				out.write(1, 0);
    			}
    			else{
    				out.write(1, 1);
    			}
    		}
    		afterSize += charCoding.length();
    	}
    	//add the PSEUDO_EOF char in the end
    	charCoding = encoder.getCode(ALPH_SIZE);
		for (int i = 0; i < charCoding.length(); i++){
			if (charCoding.charAt(i) == '0'){
				out.write(1, 0);
			}
			else{
				out.write(1, 1);
			}
		}
		afterSize += charCoding.length();
		
		// Calculate if the compressed file uses more bits
    	sizeChange = afterSize - initialSize;
    	stream.close();
    	out.close();
    	
    	
    	// Prompt the user to use force compression if the compressed file is larger
    	if (force == false && sizeChange > 0){
    		throw new IOException("Compression uses " + sizeChange + " more bits! Use Force Compression instead!");
    	}
    }
    
    /**
     * Make sure this model communicates with some view.
     * @param viewer is the view for communicating.
     */
    public void setViewer(HuffViewer viewer){
    	view = viewer;
    }
    
    /**
     * Uncompress a previously compressed file.
     * @param in is the compressed file to be uncompressed
     * @param out is where the uncompressed bits will be written
     * @throws IOException  if bad header is read or PSEUDO_EOF missing
     */
    public void uncompress(InputStream in, OutputStream out) throws IOException{
    	BitInputStream input = new BitInputStream(in);
    	BitOutputStream output = new BitOutputStream(out);
    	
    	tree.root = header.readHeader(input); // Get header info
    	decoder.initialize(tree);// recreate the huffman tree
    	decoder.doDecode(input, output);// uncompress
    }
}
