/*
 * CS 345 Proj04_RedBlack
 * 
 * Author: Joyce Wang
 * 
 * Represents a Red Black Tree. Can generate a red black tree from a given
 * 234Node tree using the alternate constructor. Otherwise has methods to 
 * set and get nodes, get inorder, postorder, and generate dot files.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Proj04_RedBlack_student<K extends Comparable<K>, V> implements Proj04_Dictionary{
	public int dotCount = 0; 
	private Proj04_Node<K,V> root; 
	private String debugStr;
	
	/*
	 * Constructor
	 * 
	 * root is set to null, debugStr used for debug purposes. 
	 */
	public Proj04_RedBlack_student(String str) {
		this.debugStr = str;
		this.root = null; 
	}
	
	/*
	 * Alternate constructor
	 * generates red black tree from 234 Nodes
	 */
	public Proj04_RedBlack_student(String str,Proj04_234Node root) {
		this.debugStr = str;
		traverse234(root);
	}
	
	/*
	 * traverses the 234 Node tree to convert to Red Black Tree
	 */
	public void traverse234(Proj04_234Node node) {
		if (node == null) {
			return; 
		}
		//add three nodes
		if (node.numKeys==3) {
			setRB(node.key2, node.val2, "b");
			setRB(node.key1, node.val1, "r");
			setRB(node.key3, node.val3, "r");
			
		}
		//add two nodes
		else if (node.numKeys==2) {
			setRB(node.key2, node.val2, "b");
			setRB(node.key1, node.val1, "r");
		}
		//add one node
		else {
			setRB(node.key1, node.val1, "b");
		}
		//go child1
		traverse234(node.child1);
		//go child2
		traverse234(node.child2); 
		//go child3
		traverse234(node.child3);
		//go child4
		traverse234(node.child4);
	}
	
	/*
	 * Set a node of  red black tree from one of the keys of a 234Tree.
	 */
	public void setRB(Comparable key, Object value, String aux) {
		root = setRB_helper(root, (K)key,(V)value, aux);
	}

	//helper function for set Red Black recursively
	private Proj04_Node<K,V> setRB_helper(Proj04_Node<K,V> oldRoot,
	                                       K key, V value, String aux)
	{
		if (oldRoot == null) {
			//add new node
			Proj04_Node<K,V> node1 = new Proj04_Node<K,V>(key,value);
			node1.aux = aux; 
			return node1;
		}
		int ct = oldRoot.key.compareTo(key); 
		//new key is smaller
		if (oldRoot.key.compareTo(key)>0) {
			oldRoot.left = setRB_helper(oldRoot.left,key,value,aux);
		}
		//new key is larger
		else if(oldRoot.key.compareTo(key)<0) {
			oldRoot.right = setRB_helper(oldRoot.right,key,value,aux);
		}
		//update height
		oldRoot.height = 1+max(height(oldRoot.left),height(oldRoot.right));
		//update count
		oldRoot.count = 1+count(oldRoot.left)+count(oldRoot.right); 
		
        return oldRoot; 
	}
	
	/*
	 * returns the color of the node
	 */
	public String color(Proj04_Node<K,V> node) {
		if (node==null) {
			return "";
		}
		return node.aux; 
	}
	
	/*
	 * returns the auxiliary value of the node
	 */
	public String aux(Proj04_Node<K,V> node) {
		if (node==null) {
			return "";
		}
		return node.aux; 
	}
	
	/*
	 * returns the count of the node
	 */
	public int count(Proj04_Node<K,V> node) {
		if (node == null) {
			return 0;
		}
		return node.count; 
	}
	
	/*
	 * returns the height of the node, -1 if null
	 */
	public int height(Proj04_Node<K,V> node) {
		if (node == null) {
			return 0;
		}
		return node.height; 
	}
	
	/*
	 * returns the max of two values, a and b
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
	 * returns the parent of a node 
	 */
	public Proj04_Node<K,V> getParent(Proj04_Node<K,V> node){
		Proj04_Node<K,V> curr = root;
		Proj04_Node<K,V> parent = null; 
		//the node is the root
		if (this.root == node) {
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
			int compare = node.key.compareTo(curr.key);
			//go left
			if (compare<0) {
				parent = curr; 
				curr = curr.left; 
			}
			else if (compare>0) {
				parent = curr; 
				curr = curr.right; 
			}
		}
		return parent;  
	}
	
	/*
	 * performs rotations to fix two red nodes touching each other
	 */
	public  Proj04_Node<K, V> fixRedRed(Proj04_Node<K,V> oldRoot, Proj04_Node<K,V> parent) {
		Proj04_Node<K,V> grandparent = getParent(parent);
		//left left
		if (grandparent.left == parent && parent.left == oldRoot) {
			grandparent.aux = "r";
			parent.aux = "b"; 
			return rotateRight(grandparent);

		}
		//right right
		else if (grandparent.right == parent && parent.right == oldRoot) {
			grandparent.aux = "r";
			parent.aux = "b";
			return rotateLeft(grandparent);
		}
		//left right
		else if (grandparent.left == parent && parent.right == oldRoot) {
			grandparent.aux = "r";
			parent.aux = "b"; 
			oldRoot = rotateLeft(oldRoot);
			return rotateRight(grandparent); 

		}
		//right left
		else if (grandparent.right == parent && parent.left == oldRoot) {
			grandparent.aux = "r";
			parent.aux = "b"; 
			oldRoot = rotateRight(oldRoot);
			return rotateLeft(grandparent); 
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#set(java.lang.Comparable, java.lang.Object)
	 * inserts a node top down into the tree, updating node colors and
	 * rotating as needed. 
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
			//System.out.println(node.key);
			//if empty tree, then make root node black
			if (oldRoot == this.root) {
				node.aux = "b";
			}
			else {
				//if leaf, then make node red
				node.aux = "r";
				/*Proj04_Node<K,V> parent = getParent(node);
				if (aux(parent).equals("r")) {
					return fixRedRed(oldRoot,parent);
				}*/
			}
			return node; 
		}
		
		int ct = oldRoot.key.compareTo(key); 
		//new key is smaller
		if (ct>0) {
			oldRoot.left = set_helper(oldRoot.left,key,value);
		}
		//new key is larger
		else if(ct<0) {
			oldRoot.right = set_helper(oldRoot.right,key,value);
		}
		//update height
		oldRoot.height = 1+max(height(oldRoot.left),height(oldRoot.right));
		//update count
		oldRoot.count = 1+count(oldRoot.left)+count(oldRoot.right); 
		//current node is black
				if (oldRoot.aux.equals("b")) {
					//if it is full, then bubble red up 
					if (aux(oldRoot.left).equals("r") && aux(oldRoot.right).equals("r")) {
						//bubble red up 
						oldRoot.aux = "r";
						oldRoot.left.aux = "b";
						oldRoot.right.aux = "b";
						//if bubbled red up to root node, make it black
						if (this.root == oldRoot) {
							oldRoot.aux = "b";
						}
						//if causes red red problem, fix it
						if (aux(getParent(oldRoot)).equals("r")) {
							return fixRedRed(oldRoot, getParent(oldRoot));
						}
					}
				}
        return oldRoot; 
	}

	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#get(java.lang.Comparable)
	 * 
	 * returns the value of the node with the given key, null if it
	 * is not in the tree.
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
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#remove(java.lang.Comparable)
	 * 
	 * Remove is not supported for this red black tree.
	 */
	@Override
	public void remove(Comparable key) {
		System.out.println("remove is not supported");	
	}

	/*
	 * (non-Javadoc)
	 * @see Proj04_Dictionary#getSize()
	 * 
	 * Returns the size of the tree
	 */
	@Override
	public int getSize() {
		if (root==null) {
			return 0;
		}
		return root.count; 
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
		if (node.aux.equals("r")) {
			writer.write(node.key+" [style = filled; color = red]"+ "\n");
		}
		else {
			writer.write(node.key+" [style = filled; color = grey]"+"\n");
		}
	}

}
