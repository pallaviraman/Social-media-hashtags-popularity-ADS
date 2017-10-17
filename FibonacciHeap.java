//package hashtagcounter;

/** 
 * Implementation of FibonacciHeap class.
 * Contains parameters max and number of nodes.
 * Contains methods FibonacciHeap(Constructor),Insert,IncreaseKey,removeMax and pairwiseCombine.
 * The parameters store the values of the node containing the maximum frequency and the number of nodes.
 * The methods implement the insertion of a new node, increasing the key of an existing node, removing the max element of the heap
 * and the pairwise combining of the same degree trees to form a FibonacciHeap after removing the max element.
 * @author Pallavi
 *
 */
public class FibonacciHeap {
	/** parameter to store the node containing the maximum key value */
	private FibonacciHeapNode max;
	/** parameter to store the number of nodes in the heap */
	private int num_nodes;
	/** Constructor to initialize the heap */
    FibonacciHeap()
    {
    	max = null;
    	num_nodes = 0;
    }
	/**
	 * Creates a new node, updates its links and frequency and adds it to the heap.
	 * Updates the max node.
	 * @param freq
	 * @return
	 */
    FibonacciHeapNode Insert(int freq)
    {
    	FibonacciHeapNode fbnode = new FibonacciHeapNode(freq);
        
        if (max != null)
        {
            fbnode.right = max;
            fbnode.left = max.left;
            max.left = fbnode;
            fbnode.left.right = fbnode;
            if(freq > max.frequency)
            {
            	max = fbnode;
            }
        }
        else
        {
            max = fbnode;
        }
        num_nodes = num_nodes + 1;
        return fbnode;
    }
    /**
     * Increases the frequency of a node by adding the new value to the current value.
     * Initiates cut and cascading cut operations if the frequency of the key becomes greater than its parent.
     * Updates the max node.
     * @param fbnode
     * @param freq
     */
    public void IncreaseKey(FibonacciHeapNode fbnode, int freq ) {
    	int newfreq = 0;
    	newfreq = freq + fbnode.frequency;
        fbnode.frequency = newfreq;
        FibonacciHeapNode parent = fbnode.parent;
        if(parent!=null && (fbnode.frequency > parent.frequency))
        {
        	parent.cut(fbnode,max);
        	parent.cascadingCut(max);
        }
        if(fbnode.frequency > max.frequency)
        {
        	max = fbnode;
        }
    }
    /**
     * Removes the node containing the maximum element.
     * Initiates Pairwise combine operation of the children of the maximum element and the root list.
     * @return the maximum Fibonacci Heap node
     */
    public FibonacciHeapNode removeMax() {
    	FibonacciHeapNode temp = max;
        if (temp == null) {
            return null;
        }
        if (temp.child != null) {
            temp.child.parent = null;
            for (FibonacciHeapNode maxchild = temp.child.right; maxchild != temp.child; maxchild = maxchild.right) {
                maxchild.parent = null;
            }
            FibonacciHeapNode maxleft = max.left;
            FibonacciHeapNode maxchildleft = temp.child.left;
            max.left = maxchildleft;
            maxchildleft.right = max;
            temp.child.left = maxleft;
            maxleft.right = temp.child;
        }
        temp.left.right = temp.right;
        temp.right.left = temp.left;
        if (temp == temp.right) {
            max = null;
        } else {
            max = temp.right;
            pairwiseCombine();
        }
        num_nodes--;
        return temp;
    }
    /**
     * Combines the same degree trees into a Fibonacci Heap by making one tree a child of another after comparing the frequency values.
     * Updates the max node.
     */
    private void pairwiseCombine()
    {
    	 FibonacciHeapNode[] childArray = new FibonacciHeapNode[java.lang.Math.round(num_nodes)];
    	 FibonacciHeapNode start = max;
    	 FibonacciHeapNode temp = max;
    	 do{
    		 FibonacciHeapNode temp1 = temp;
    		 FibonacciHeapNode tempright = temp.right;
    		 int deg = temp1.degree;
	    	 while(childArray[deg] != null)
	    	 {
	    		FibonacciHeapNode temp2 = childArray[deg] ;
	    		if(temp2.frequency > temp1.frequency)
	    		{
	    			FibonacciHeapNode swap = temp2;
	    			temp2 = temp1;
	    			temp1 = swap;
	    		}
	    		if(temp2 == start)
	    		{
	    			 start = start.right;
	    		}
	    		if(temp2 == tempright)
	    		{
	    			tempright = tempright.right;
	    		}
	    		temp2.linkNodes(temp1);
	    		childArray[deg] = null ;
	    		deg++;
	    	}
	    	childArray[deg] = temp1;
    		temp = tempright;
    		 
    	 }while(temp !=start);
    	 
    	 max = start;
    	 for(FibonacciHeapNode itr : childArray)
    	 {
    		 if(itr != null && itr.frequency > max.frequency)
    		 {
    			 max = itr;
    		 }
    	 }
    }
    
}
/**
 * Implementation of FibonacciHeapNode class.
 * Contains the params frequency,child,left,right,parent,degree,is_cut.
 * Contains the methods FibonacciHeapNode(Constructor),cut,cascadingCut and linkNodes.
 * The params store the child,parent,left and right pointers,frequency value, degree value and is_cut field to check if any of its child has been cut before.
 * The methods implement the constructor which initializes the properties of a node, cut which cuts the node from its siblings,
 * cascading cut if is_cut value of its parent is true and linkNodes which links the children into a doubly linked list.
 * @author Pallavi
 *
 */
class FibonacciHeapNode {
	/** count of number of occurrences of the key */
	int frequency;
	/** denotes the child pointer */
	FibonacciHeapNode child;
	/** denotes the left pointer */
	FibonacciHeapNode left;
	/** denotes the right pointer */
	FibonacciHeapNode right;
	/** denotes the parent pointer */
	FibonacciHeapNode parent;
	/** number of children of the node */
	int degree;
	/** denotes whether any of the children of the node is removed or not */
	boolean is_cut;
	/**
	 * Constructor.Intializes the pointers and values of a newly formed node.
	 * @param freq
	 */
	public FibonacciHeapNode(int freq)
	{
		this.frequency = freq;
	    this.left = this;
	    this.right = this;
	    this.parent = null;
	    this.child = null;
	    this.degree = 0;
	    this.is_cut = false;
	}
	/**
	 * Removes the node whose value has become greater than its parent after increasekey operation.
	 * sets the is_cut as false, parent as null.
	 * @param fbnode
	 * @param max
	 */
	public void cut(FibonacciHeapNode fbnode,FibonacciHeapNode max)
	{
		fbnode.left.right = fbnode.right;
		fbnode.right.left = fbnode.left;
		degree--;
		if(degree == 0)
		{
			child = null;
		}
		else if(child == fbnode)
		{
			child = fbnode.right;
		}
		
		fbnode.right = max;
		fbnode.left = max.left;
		max.left = fbnode;
		fbnode.left.right = fbnode;
		fbnode.parent = null;
		fbnode.is_cut = false;		
	}
    /**
     * checks the is_cut parameter of the parent of a node, which is cut after increase key operation.
     * If the parent has is_cut value as true,cascading cut is done and the parent will also be removed just like the actual node.
     * The is_cut parameter checking is propagated upwards through parent pointer until it reaches root list.
     * @param max
     */
	public void cascadingCut(FibonacciHeapNode max) {
        FibonacciHeapNode gparent = parent;
 
        if (gparent != null) {
            if (is_cut) {
                gparent.cut(this, max);
                gparent.cascadingCut(max);
            } else {
            	is_cut = true;
            }
        }
    }
	/**
	 * Links the children of a node in a doubly linked list as part of pairwise combine operation. 
	 * @param pointer to the node which is to be made parent while linking.
	 */
	public void linkNodes(FibonacciHeapNode parent)
	{
		left.right = right;
		right.left = left;
		this.parent = parent;
		if(parent.child == null)
		{
			parent.child = this;
			right = this;
			left = this;
		}
		else
		{
			left = parent.child;
			right = parent.child.right;
			parent.child.right = this;
			right.left = this;
		}
		parent.degree++;
		is_cut = false;
	}
	

	
}
