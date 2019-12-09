package tree;

import java.util.ArrayList;

public class BiTreeNode {
	private Entry e;  //e is entry in BST
	protected BiTreeNode parent, l, r; // left/right child
	private int size,height,depth; // in T tree
	// extra attributes for tango tree
	private boolean mark; // whether node is a root in the auxiliary tree
	private int depthP;  // the depth of node in P tree, !! not in T tree
	private int maxDepth, minDepth; // the max/min depth: depth in P tree. Consider all nodes in its T subtree
	
	public BiTreeNode() {};
	// asLChild: if node is left child
	public BiTreeNode(Entry e, BiTreeNode parent, BiTreeNode l,BiTreeNode r, boolean asLChild, boolean mark, int depthP) {
		this.e=e;
		this.l=l; this.r=r; this.parent=parent; 
		size=1; height=0; depth=0;
		// connect parent and this node
		if (parent != null) {
			if (asLChild) parent.attachL(this);
			else parent.attachR(this);
		}
		// connect this node with children
		if (l != null) attachL(l);
		if (r != null) attachR(r);
		this.mark=mark;
		this.depthP=depthP; 
	}
	public BiTreeNode(int key,Object value, boolean mark) {
		this.e=new Entry(key,value);
		parent=null;
		l=null;
		r=null;
		this.mark=mark;
	}
	public BiTreeNode(int key,Object value, boolean mark,int depthP) {
		this.e=new Entry(key,value);
		parent=null;
		l=null;
		r=null;
		this.mark=mark;
		this.depthP=depthP;
	}
	public BiTreeNode(Entry e, boolean mark,int depthP) {
		this.e=e;
		parent=null;
		l=null;
		r=null;
		this.mark=mark;
		this.depthP=depthP;
	}

	///// for tango tree
	public boolean isMarked() {
		return mark;
	}
	public void setMark(boolean b) {
		mark=b;
	}
	public int getMaxDepth() {
		return maxDepth;
	}
	public int getMinDepth() {
		return minDepth;
	}
	// depth of corresponding node in P
	public int getDepthP() {
		return depthP;
	}
	public void setDepthP(int d) {
		depthP=d;
	}
	public void updateMaxDepth() {
		maxDepth=getDepth();
		if (hasLChild()) maxDepth=Math.max(maxDepth,(getLChild()).maxDepth);
		if (hasRChild()) maxDepth=Math.max(maxDepth,(getRChild()).maxDepth);
		if (hasParent()) (getParent()).updateMaxDepth();
	}
	public void updateMinDepth() {
		minDepth=getDepth();
		if (hasLChild()) minDepth=Math.min(minDepth,(getLChild()).minDepth);
		if (hasRChild()) minDepth=Math.max(minDepth,(getRChild()).minDepth);
		if (hasParent()) (getParent()).updateMinDepth();
	}
	
	///// for general BST
	public Entry getE() {
		return e;
	}
	public void setE(Entry e) {
		this.e=e;
	}
	public int getKey() {
		return getE().getKey();
	}
	public void setKey(int k) {
		getE().setKey(k);
	}
	public Object getValue() {
		return getE().getValue();
	}
	public void setValue(Object v) {
		getE().setValue(v);
	}
	// parent
	public boolean hasParent() {
		return parent != null;
	}
	public BiTreeNode getParent() {
		return parent;
	}
	public void setParent(BiTreeNode p) {
		parent=p;
	}
	// leaf
	public boolean isLeaf() {
		return !hasLChild() && !hasRChild();
	}
	public boolean isLChild() {
		return hasParent() && this==getParent().getLChild();
	}
	public boolean isRChild() {
		return hasParent() && this==getParent().getRChild();
	}
	// children
	public boolean hasLChild() {
		return l!=null;
	}
	public BiTreeNode getLChild() {
		return l;
	}
	public void setLChild(BiTreeNode l) {
		this.l=l;
	}
	public boolean hasRChild() {
		return r!=null;
	}
	public BiTreeNode getRChild() {
		return r;
	}
	public void setRChild(BiTreeNode r) {
		this.r=r;
	}
	// size, # of children plus 1
	public int getSize() {
		return size;
	}
	public void updateSize() {
		size=1;
		if (hasLChild()) size+=l.getSize();
		if (hasRChild()) size+=r.getSize();
		if (hasParent()) parent.updateSize(); // ancestor change size
	}
	// height
	public int getHeight() {
		return height;
	}
	/*  for normal BST
	public void updateHeight() {
		height=0;
		if (hasLChild()) height=Math.max(height, l.getHeight()+1);
		if (hasRChild()) height=Math.max(height, r.getHeight()+1);
		if (hasParent()) parent.updateHeight(); // ancestor change size
	}*/
	// in tango tree, the height update is different from binary tree, 
	//when it child is marked, the height of this corresponding path is set to 0
	public void updateHeight() {
		height=0;
		if (hasLChild()) {
			// if left child is marked, height is 0 from left side
			if (l.isMarked()) height=0; 
			else {
				height=Math.max(height, l.getHeight()+1);
			}
		}
		if (hasRChild()) {
			// if right child is marked, it will not affect height, otherwise, compare left with right and set max as height
			if (r.isMarked()==false) 
				height=Math.max(height, r.getHeight()+1);
		}
		if (hasParent()) parent.updateHeight(); // ancestor change size
	}
	// depth
	public int getDepth() {
		return depth;
	}
	public void updateDepth() {
		depth=0;
		if (hasParent()) depth=1+parent.getDepth();

		if (hasLChild()) l.updateDepth();
		if (hasRChild()) r.updateDepth();  // change children downward
	}
	// get previous node
	public BiTreeNode getPrev() {
		if (hasLChild()) {
			if (!l.isMarked()) return findMax(l); // the max in left child
		}
		if (isRChild()) return getParent(); // for right child, return parent
		// node is left child, without left child or has a marked left child
		BiTreeNode t=this;
		while (t.isLChild()) t=t.getParent();
		return t.getParent();  // until t is a right child
	} 
	// get successor
	public BiTreeNode getSucc() {
		if (hasRChild()) 
			if ((!r.isMarked()))
				return findMin(r);
		if (isLChild()) return getParent(); 
		BiTreeNode t=this;
		while (t.isRChild()) t=t.getParent();
		return t.getParent();  // until t is a left child 
	}
	// cut node from parent
	public void secede() {
		if (parent != null) {
			if (isLChild()) parent.setLChild(null);
			else parent.setRChild(null);  // disconnect node with parent
			// update data for parent and node
			parent.updateSize();
			parent.updateHeight();
			
			parent=null;
			updateDepth();
		}
	}
	// remove original child and attach new node
	public void attachL(BiTreeNode n) {
		if (hasLChild()) l.secede(); // detach original left child
		if (n != null) {
			n.secede();
			l=n;
			n.setParent(this);
			updateSize();
			updateHeight();
			n.updateDepth();
		}
	}
	public void attachR(BiTreeNode n) {
		if (hasRChild()) r.secede(); // detach original right child
		if (n != null) {
			n.secede();
			r=n;
			n.setParent(this);
			updateSize();
			updateHeight();
			n.updateDepth();
		}	
	}
	// inorder traversal
	public ArrayList<BiTreeNode> elementsInorder() {
		ArrayList<BiTreeNode> list=new ArrayList<>();
		inorder(list,this);
		return list;
	}
	// find min/max of tree rooted at n
	public BiTreeNode findMin(BiTreeNode n) {
		if (n != null)
			while (n.hasLChild()) 
				if(!n.getLChild().isMarked())
					n=n.getLChild();  // return the most left child
				else break;
		return n;
	}
	public BiTreeNode findMax(BiTreeNode n) {
		if (n != null)
			while (n.hasRChild()) 
				if(!n.getRChild().isMarked())
					n=n.getRChild();  // return the most left child
				else break;
		return n;
	}
	public static void inorder(ArrayList<BiTreeNode> list,BiTreeNode n) {
		if (n==null) return;
		// first left, then root, then right
		inorder(list,n.getLChild());
		list.add(n);
		inorder(list,n.getRChild());
	}
}
