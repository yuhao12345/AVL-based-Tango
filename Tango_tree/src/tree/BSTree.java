package tree;

import java.util.ArrayList;

public class BSTree extends BinTree{
	public static int o=0;  // cost counter
	protected BiTreeNode last; //last operation node
	public BSTree() {super();}
	public BSTree(BiTreeNode root) {
		super(root);
	}
	//search key, return content
	public Entry search(int key) {
		if (isEmpty()) return null;
		BiTreeNode n=binSearch(getRoot(),key);
		return (key==n.getKey()) ? (Entry)n.getE() : null;
	}
	public BiTreeNode getLast() {
		return last;
	}
	
	public static void resetO() {
		o=0;
	}
	
	// insert, last hook is the newly inserted node
	public void insert(int key, Object value, boolean mark,int depthP) {
		Entry e=new Entry(key,value);
		boolean asL;
		if (isEmpty()) {   // empty, then inserted node is root
			root=new BiTreeNode(e, null, null,null, true, mark,depthP);
			last=root;
		}
		else {
			BiTreeNode x;
			x=binSearch(root,key); // search key in tree and get result r
			while(true) {
				if (x.getKey()>key) {asL=true;break;}
				else if (x.getKey()<key) {asL=false;break;}  //new node is right child 
				else if (x.hasLChild()==false) {asL=true;break;}
				else if (x.hasRChild()==false) {asL=false;break;} //new node is l/r if original tree has no l/r child
				else x=x.getLChild();	
				x=binSearch(x,key); 
			}
			//BiTreeNode(Entry e, BiTreeNode parent, BiTreeNode l,BiTreeNode r, boolean asLChild, boolean mark, int depthP)
			//last=new BiTreeNode(e,x,null,null,asL, mark,depthP);
			last=new BiTreeNode(key, value, mark,depthP);
			if (asL) x.attachL(last);
			else x.attachR(last);
			last.updateSize();
		}
	}
	public BiTreeNode remove(int key) {
		if(isEmpty()) return null;
		BiTreeNode r= root;
		BiTreeNode v=binSearch(r,key);
		if (v.getKey()!=key) return null;  // cannot find key
		if (v.hasLChild()){
			// find predecessor w in v.l, 
			BiTreeNode w=v.getPrev();  // w has no right child
			// exchange w and v
			Entry tmp=w.getE();
			w.setE(v.getE());
			v.setE(tmp);
			v=w;  // denote the w as v
		}
		// v has only one child, remove v, and replace v with its child
		last=v.getParent();
		if (v.isLeaf()) v.secede();
		else if(v.hasLChild()) {
			BiTreeNode vc=v.getLChild();
			v.secede();
			last.attachL(vc);
		}else {
			BiTreeNode vc=v.getRChild();
			v.secede();
			last.attachR(vc);
		}
		return v;
	}
	// return target or nearest leaf
	public static BiTreeNode binSearch(BiTreeNode n, int key) {
		if (key==n.getKey()) return n;
		else if (key<n.getKey()) {
			if (n.hasLChild())
				return binSearch(n.getLChild(),key);
			return n;
		}
		else {
			if (n.hasRChild())
				return binSearch(n.getRChild(),key);
			return n;
		}
	}
	
	public ArrayList<BiTreeNode> TreeToList(BiTreeNode r) {
		ArrayList<BiTreeNode> list=new ArrayList<>();
		concatenate(list,r);
		return list;
	}
	public void concatenate(ArrayList<BiTreeNode> list,BiTreeNode n) {
		if (n==null) return;
		concatenate(list,n.getLChild());
		list.add(n);
		concatenate(list,n.getRChild());
	}
	public String toString() {
		String s="The key list of tree (inorder):";
		ArrayList<BiTreeNode> list=TreeToList(getRoot());
		for (int i=0;i<list.size();i++) {
			s+=list.get(i).getKey()+" ";
		}
		return s;
	}
}
