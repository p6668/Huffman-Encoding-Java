import java.util.Comparator;

/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Class for the priority queue ADT for creating the huffman tree
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 * @author Zifan Yang
 */
public class TreeNodePriorityQueue{
	
  private PositionalList<TreeNode> list = new LinkedPositionalList<>();
 
  /**
   * Returns the number of items in the priority queue.
   * @return number of items
   */
  int size(){return list.size();}
  
  /**
   * Inserts a TreeNode and returns the node created.
   * @param node     the new node to add
   * @return the added node
   * @throws IllegalArgumentException if the key is unacceptable for this queue
   */
  TreeNode insert(TreeNode node) throws IllegalArgumentException {
	list.addLast(node);
	return node;
}

  /**
   * Returns (but does not remove) a node with minimal key.
   * @return entry having a minimal key (or null if empty)
   */
  TreeNode min(){
	if (list.isEmpty()) return null;
	return findMin().getElement();
	  
  }
  /**
   * Returns the position of an node with minimal key 
   */
  private Position<TreeNode> findMin() {
	Position<TreeNode> temp = list.first();
	for (Position<TreeNode> walk : list.positions()){
		if(walk.getElement().compareTo(temp.getElement()) <= 0){
			temp = walk;
		}
	}
	return temp;
}

/**
   * Removes and returns an node with minimal key.
   * @return the removed node (or null if empty)
   */
  TreeNode removeMin(){
	  if (list.isEmpty()) return null;
	  return list.remove(findMin());
  }


  /**
   * Tests whether the priority queue is empty.
   * @return true if the priority queue is empty, false otherwise
   */

  public boolean isEmpty() { return size() == 0; }
}
