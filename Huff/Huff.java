
/**
 * Main/launch program for Huff assignment. A better
 * comment than this is warranted.
 * @author Zifan Yang
 *
 */
public class Huff {

    public static void main(String[] args){
        HuffViewer sv = new HuffViewer("Huffing Coding");
        IHuffModel hm = new HuffModel();
        sv.setModel(hm);    
    }
}
