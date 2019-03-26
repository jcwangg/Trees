/**
 * CS 345 Proj04_AVL 
 * 
 * Author: Joyce Wang
 * 
 * Represents an AVL tree where difference in height between subtrees of a node cannot be more than 1.
 * Has methods to get and set nodes, remove nodes, get inorder, postorder, and a dot file for debugging.
 * 
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Proj04_AVL_student<K extends Comparable<K>, V> implements Proj04_Dictionary {
	public int dotCount; 
	private Proj04_Node<K,V> root; 
	private String debugStr;
	
	/*
	 * Constructor
	 * 
	 * debugStr and dotCount used to name dot files. Root set to null. 
	 */
	public Proj04_AVL_student(String str){
		this.debugStr = str;
		this.root = null; 
		this.dotCount = 0; 
	}
	
	/*
	 * takes int a and int b and returns the max of the two. 
	 */
	public int max (int a, int b) {
		if (a<b) {
			return b; 
		}
		else {
			return a; 
		}
	}
	
	/*
	 * Returns the height of a node. If null, return -1. 
	 */
	public int height(Proj04_Node<K,V> node) {
		if (node == null) {
			return -1;
		}
		return node.height; 
	}
	
	 /* 
	  * Get Balance factor of node N to check height requirement
	  */
    int getBalance(Proj04_Node<K,V> node) { 
        if (node == null) 
            return 0; 
  
        return height(node.left) - height(node.right); 
    }
    
    /*
     * get count of all nodes in that subtree
     */
	public int count(Proj04_Node<K,V> node) {
		if (node == null) {
			return 0;
		}
		return node.count; 
	}
	
	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#set(java.lang.Comparable, java.lang.Object)
	 * 
	 * Inserts a new node into the tree, rebalancing when needed to maintain height requirements. 
	 */
	@Override
	public void set(Comparable key, Object value) {
		root = set_helper(root, (K)key,(V)value);
	}

	//helper function for set recursively
	private Proj04_Node<K,V> set_helper(Proj04_Node<K,V> oldRoot,
	                                       K key, V value)
	{
		if (oldRoot == null) {
			//add new node
			Proj04_Node<K,V> node = new Proj04_Node<K,V>(key,value);
			node.height = 0;
			node.aux ="h="+String.valueOf(node.height);
			return node;

		}
		int ct = oldRoot.key.compareTo(key); 
		//new key is smaller
		if (oldRoot.key.compareTo(key)>0) {
			oldRoot.left = set_helper(oldRoot.left,key,value);
		}
		//new key is larger
		else if(oldRoot.key.compareTo(key)<0) {
			oldRoot.right = set_helper(oldRoot.right,key,value);
		}
		else { //key already in tree, update value
			oldRoot.value = value; 
			return oldRoot; 
		}
		//update height and aux
		oldRoot.height = 1+max(height(oldRoot.left),height(oldRoot.right));
		oldRoot.aux = "h="+String.valueOf(oldRoot.height);
		//update count
		oldRoot.count = 1+count(oldRoot.left)+count(oldRoot.right); 
		//get balance factor- four cases
		int balance = getBalance(oldRoot);
	     // Left Left Case 
        if (balance > 1 && key.compareTo(oldRoot.left.key)<0) {
            return rotateRight(oldRoot); 
        }
        // Right Right Case 
        if (balance < -1 && key.compareTo(oldRoot.right.key)>0) {
            return rotateLeft(oldRoot); 
        }
        // Left Right Case 
        if (balance > 1 && key.compareTo(oldRoot.left.key)>0) { 
            oldRoot.left = rotateLeft(oldRoot.left); 
            return rotateRight(oldRoot); 
        } 
        // Right Left Case 
        if (balance < -1 && key.compareTo(oldRoot.right.key)<0) { 
            oldRoot.right = rotateRight(oldRoot.right); 
            return rotateLeft(oldRoot); 
        } 
        return oldRoot; 
	}
	
	/**
	 * Returns the value of a key in the tree, null if not found
	 */
	@Override
	public Object get(Comparable key) {
		return get_helper(root,(K)key);
	}

	//helper function for get recursion
	private V get_helper(Proj04_Node<K,V> currNode, K key) {
		if (currNode == null) {
			return null;
		}
		int compare = key.compareTo(currNode.key);
		//go left
		if (compare < 0) {
			return get_helper(currNode.left,key);
		}
		//go right
		else if (compare > 0) {
			return get_helper(currNode.right,key);
		}
		//found the node
		else {
			return currNode.value;
		}
	}

	/*
	 * gets predecessor of node for removal purposes. 
	 */
	public Proj04_Node<K,V> getPred(Proj04_Node<K,V> node) {
		Proj04_Node<K,V> curr = node.left; 
		while (curr.right !=null) {
			curr = curr.right; 
		}
		return curr; 
	
	/*
	 * removes the node with the given key from the tree, returns null if node doesn't exist. 
	 * performs balance rotations if necessary after removal. 
	 */
	}
	@Override
	public void remove(Comparable key) {
		root = remove_helper(root,(K)key);
		
	}

	//helper function for remove recursively
	private Proj04_Node<K,V> remove_helper (Proj04_Node<K,V> root, K key){
		//base case, key doesn't exist
		if (root == null) {
			return root;
		}
		int compare = key.compareTo(root.key);
		//go left
		if (compare < 0) {
			root.left = remove_helper(root.left,key);
		}
		//go right
		else if (compare > 0) {
			root.right = remove_helper(root.right,key);
		}
		//delete
		else {
			//1 or no children
			if (root.left == null || root.right == null) {
				Proj04_Node<K,V> temp = null;
				if (temp == root.right) {
					temp = root.left;
				}
				else {
					temp = root.right; 
				}
				//no children
				if (temp == null) {
					temp = root;
					root = null; 
				}
				//one child
				else {
					root = temp; 
				}
			}
			else {
				//get predecessor
				Proj04_Node<K,V> pred = getPred(root); 
				//swap root with predecessor
				root.key = pred.key;
				root.value = pred.value; 
				root.left = remove_helper(root.left,pred.key); 
			} 
		}
		//if tree is now empty then return null 
		if (root == null) {
			return root; 
		}
		//update height
		root.height = 1+max(height(root.left),height(root.right));
		root.aux = "h="+String.valueOf(root.height);
		//update count
		root.count = 1+count(root.left)+count(root.right); 
		//get balance factor
		int balance = getBalance(root); 
		// Left Left Case  
        if (balance > 1 && getBalance(root.left) >= 0)  {
            return rotateRight(root);  
        }
        // Left Right Case  
        if (balance > 1 && getBalance(root.left) < 0)  {  
            root.left = rotateLeft(root.left);  
            return rotateRight(root);  
        }  
        // Right Right Case  
        if (balance < -1 && getBalance(root.right) <= 0)  {
            return rotateLeft(root);  
        }
        // Right Left Case  
        if (balance < -1 && getBalance(root.right) > 0)  {
            root.right = rotateRight(root.right);  
            return rotateLeft(root);  
        }  
		return root;
	}

	/* void rotateLeft(K)
	 *
	 * Finds a node with given K key and performs left rotation at
	 * that node. Updates the counts after. 
	 *
	 */
	public Proj04_Node<K,V> rotateLeft(Proj04_Node<K,V> at) {
		Proj04_Node<K,V> y = at.right;
		Proj04_Node<K,V> t2 =at.right.left; 
		//do rotation
		y.left = at; 
		at = y; 
		at.left.right = t2; 
		//update heights
		at.left.height = 1+max(height(at.left.left),height(at.left.right));
		at.left.aux = "h="+String.valueOf(at.left.height);
		at.height = 1+max(height(at.left),height(at.right));
		at.aux = "h="+String.valueOf(at.height);
		//update counts
		at.left.count = 1+count(at.left.left)+count(at.left.right);
		at.count = 1+count(at.left)+count(at.right);
		return at; 
		
	}

	/* void rotateRight(K)
	 *
	 * Finds a node with given K key and performs right rotation at
	 * that node. Updates the counts after. 
	 *
	 */
	public Proj04_Node<K,V> rotateRight(Proj04_Node<K,V>  at) {
		Proj04_Node<K,V> y = at.left;
		Proj04_Node<K,V> t2 =at.left.right; 
		//do rotation
		y.right = at; 
		at = y; 
		at.right.left = t2; 
		//update heights
		at.right.height = 1+max(height(at.right.left),height(at.right.right));
		at.right.aux = "h="+ String.valueOf(at.right.height);
		at.height = 1+max(height(at.left),height(at.right));
		at.aux = "h="+String.valueOf(at.height);
		//update counts
		at.right.count = 1+count(at.right.left)+count(at.right.right);
		at.count = 1+count(at.left)+count(at.right);
		return at; 
	}
	
	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#getSize()
	 * 
	 * Returns the size of the tree. 
	 */
	@Override
	public int getSize()
	{
		return getCount(root);
	}
	
	/*
	 * returns the count of a node. 
	 */
	private int getCount(Proj04_Node<K,V> node)
	{
		if (node == null)
			return 0;
		return node.count;
	}

	/*
	 * returns the parent node of a given node, and null if it has no parent.
	 */
	public Proj04_Node<K,V> getParent(Proj04_Node<K,V> node){
		Proj04_Node<K,V> curr = root;
		//the node is the root
		if (curr == node) {
			return null;
		}
		while(curr!=null) {

			//found parent
			if (curr.left == node) {
				return curr; 
			}
			if (curr.right == node) {
				return curr; 
			}
				System.out.println(curr);
				System.out.println(node);
			int compare = node.key.compareTo(curr.key);
			if (compare<0) {
				curr = curr.left; 
			}
			else if (compare>0) {
				curr = curr.right; 
			}
		}
		return null; 
	}
	
	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#inOrder(java.lang.Comparable[], java.lang.Object[], java.lang.String[])
	 * 
	 * gets the inorder traversal of keys, values, and aux values of all nodes. 
	 */
	@Override
	public void inOrder(Comparable[] keysOut, Object[] valuesOut, String[] auxOut) {
		//stores nodes in inorder in an ArrayList
		ArrayList<Proj04_Node<K,V>> inorder = new ArrayList<Proj04_Node<K,V>>();
		inOrder_helper(root, inorder); 
		//transfer correct values to the three arrays
		for (int i = 0; i<inorder.size();i++) {
			keysOut[i] = inorder.get(i).key; 
			valuesOut[i] = inorder.get(i).value;
			auxOut[i] = inorder.get(i).aux;
		}
	}
	
	//helper function to get inorder recursively
	private void inOrder_helper (Proj04_Node<K,V> root, ArrayList<Proj04_Node<K,V>> inorder) {
		if (root == null) {
			return;
		}
		//go left
		inOrder_helper(root.left, inorder);
		//add node
		inorder.add(root);
		//go right
		inOrder_helper(root.right,inorder); 
	}

	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#postOrder(java.lang.Comparable[], java.lang.Object[], java.lang.String[])
	 * 
	 * Gets the postorder traversal of keys, values, and aux values of all nodes. 
	 */
	@Override
	public void postOrder(Comparable[] keysOut, Object[] valuesOut, String[] auxOut) {
		//stores postorder of the nodes in an ArrayList
		ArrayList<Proj04_Node<K,V>> postorder = new ArrayList<Proj04_Node<K,V>>();
		postOrder_helper(root, postorder); 
		//transfer values from arrayList to the three arrays
		for (int i = 0; i<postorder.size();i++) {
			keysOut[i] = postorder.get(i).key; 
			valuesOut[i] = postorder.get(i).value;
			auxOut[i] = postorder.get(i).aux;
		}	
	}
	
	//helper function to get postorder recursively
	private void postOrder_helper (Proj04_Node<K,V> root, ArrayList<Proj04_Node<K,V>> postorder) {
		if (root == null) {
			return;
		}
		//go left
		postOrder_helper(root.left, postorder);
		//go right
		postOrder_helper(root.right,postorder); 
		//add node
		postorder.add(root);
	}

	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#genDebugDot()
	 * 
	 * Generates a dot file for debugging of the current tree. 
	 */
	@Override
	public void genDebugDot() {
		String dot=""; 
		try {
			File file = new File(dotCount+debugStr+".dot");
			dotCount++; 
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write("digraph\n{\n");
			dot_helper(writer,root); 
			writer.write("}"); 
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not create dot file");
		}
	}
	
	//helper function for writing dot file recursively
	private void dot_helper (FileWriter writer, Proj04_Node<K,V> node) throws IOException  {
		if (node == null) {
			return;
		} 
		if (node.left!= null) {
			writer.write(node.key+"->"+node.left.key+"\n");
		}
		if (node.right!=null) {
			writer.write(node.key+"->"+node.right.key+"\n");
		}
		dot_helper(writer,node.left);
		dot_helper(writer,node.right);
		writer.write(node.key+"\n");
	}

}
