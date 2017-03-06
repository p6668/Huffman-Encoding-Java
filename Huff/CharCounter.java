import java.io.IOException;
import java.io.InputStream;
/**
 * A class that implements the ICharCounter interface
 * Methods in this class are used to count characters in HuffModel
 * @author Zifan Yang
 *
 */
public class CharCounter implements ICharCounter {
	private int list[];
	public CharCounter(){
		list = new int[IHuffHeader.ALPH_SIZE];
		for (int i = 0; i < IHuffHeader.ALPH_SIZE; i++){
			list[i] = 0;
		}
	}
	 /**
     * Returns the count associated with specified character.
     * @param ch is the chunk/character for which count is requested
     * @return count of specified chunk
     * @throws some kind of exception if ch isn't a valid chunk/character
     */
    public int getCount(int ch) throws IllegalArgumentException{
    	if (ch < 0 || ch > 255){
    		throw new IllegalArgumentException("Invalid character!");
    	}
    	return list[ch];
    }
    
    /**
     * Initialize state by counting bits/chunks in a stream
     * @param stream is source of data
     * @return count of all chunks/read
     * @throws IOException if reading fails
     */
    public int countAll(InputStream stream) throws IOException{
    	clear();
    	int bits = 0;
    	int returnVal = 0;
    	while((bits = stream.read()) != -1){
    		returnVal += IHuffConstants.BITS_PER_WORD;
    		add(bits);
    	}
    	return returnVal;
    }
    
    /**
     * Update state to record one occurrence of specified chunk/character.
     * @param i is the chunk being recorded
     */
    public void add(int i){
    	list[i] = list[i] + 1;
    }
    
    /**
     * Set the value/count associated with a specific character/chunk.
     * @param i is the chunk/character whose count is specified
     * @param value is # occurrences of specified chunk
     */
    public void set(int i, int value){
    	list[i] = value;
    }
    
    /**
     * All counts cleared to zero.
     */
    public void clear(){
    	for (int i = 0; i < IHuffHeader.ALPH_SIZE; i++){
			list[i] = 0;
		}
    }
}
