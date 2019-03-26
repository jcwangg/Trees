
public class Proj04_Node <K extends Comparable, V>{
	public  K  key;
	public  V  value;
	public int count;
	public int height; 
	public String aux;

	public Proj04_Node<K,V> left,right;


	/* constructor
	 *
	 * Parameters: K,V
	 *
	 * Generates a single BSTNode (with no children), with the key/value
	 * pair given.
	 */
	public Proj04_Node(K key, V value)
	{
		if (value == null)
			throw new IllegalArgumentException("BSTNode: The 'value' parameter was null");

		this.key   = key;
		this.value = value;

		this.count = 1;
		this.height = 1; 
		this.aux="";
		// the left,right nodes default to 'null' (Java defaults)
	}

}
