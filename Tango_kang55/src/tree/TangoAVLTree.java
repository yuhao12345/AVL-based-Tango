package tree;

import java.util.ArrayList;
import java.util.Random;

public class TangoAVLTree extends BSTree{
	public TangoAVLTree() {super();}
	public TangoAVLTree(BiTreeNode r) {super(r);}
	
	public static TangoAVLTree initializeTangoTree(int n) {
		TangoAVLTree t=new TangoAVLTree(); 
		Random rnd=new Random();
		for (int i=0;i<n;i++) {
			int temp=rnd.nextInt(100000);
			t.insert(temp, 0, false, 0);
			BiTreeNode last=t.getLast();
			last.setDepthP(last.getDepth());
			// at the beginning, every node is aux tree of itself
			last.setMark(true);   
		}
		t.root.setMark(false); // the root of top aux tree is not marked
		return t;		
	}
	public static TangoAVLTree initializeTangoTree2(int n) {
		TangoAVLTree t=new TangoAVLTree(); 
		for (int i=0;i<n;i++) {
			t.insert(i, 0, false, 0);
			BiTreeNode last=t.getLast();
			last.setDepthP(last.getDepth());
			// at the beginning, every node is aux tree of itself
			last.setMark(true);   
		}
		t.root.setMark(false); // the root of top aux tree is not marked
		return t;		
	}
	// In the top aux tree rooted at r, cut n's later path, which exclude n itself
	// in P tree, it means cut any nodes deeper than n
	// return the root of new top aux tree
	public BiTreeNode cut(BiTreeNode r, BiTreeNode n) { 
		int d=n.getDepthP();  // depth of node n in P
		return cutAtDepth(r,d);
	}
	// in P tree, any nodes deeper (strictly larger) than depth d must fall in (lb2,rb2)
	// which is equivalent to cut nodes within the range (lb2,rb2)
	// first get closed boundary [lb,rb], than get (lb2,rb2)
	// r is the root of original top aux tree, return new root
	public BiTreeNode cutAtDepth(BiTreeNode r, int d) {
		BiTreeNode lb2,rb2;
		BiTreeNode lb=minNodeDeeperThan(r, d);  // for P
		BiTreeNode rb=maxNodeDeeperThan(r, d);
		if (lb==null) lb2=null;
		else lb2=lb.getPrev();
		if (rb==null) rb2=null;
		else rb2=rb.getSucc();
		return cutWithinLR(r,lb2,rb2);  // cut T
	}
	
	//in P tree, return minimum node deeper(>) than d. 
	// 1) if root.maxDepth<=d, it means no children in r is deeper than depth d, return null
	// 2) else, if r does not have left child or left child is marked, go to r.right
	// 3) r has left child, if its left child's maxDepth<=d, return r; otherwise go to r.left
	public BiTreeNode minNodeDeeperThan(BiTreeNode r, int d) {
		if (r==null) return null;
		if (r.getMaxDepth()<=d) return null;
		if (!r.hasLChild()) {
			if (!r.hasRChild()) return r;
			if (r.getRChild().isMarked()) return r;
			if (r.getRChild().getMaxDepth()<=d) return r;
			r=r.getRChild();
		}else if(r.getLChild().isMarked() || r.getLChild().getMaxDepth()<=d){
			if (!r.hasRChild()) return r;
			if (r.getRChild().isMarked()) return r;
			if (r.getRChild().getMaxDepth()<=d) return r;
			r=r.getRChild();
		}else {
			r=r.getLChild();
		}
		return minNodeDeeperThan(r, d);
	}
	// in P tree, search max node deeper (>) than depth d
	// find closed boundary n
	public BiTreeNode maxNodeDeeperThan(BiTreeNode r, int d) {
		if (r==null) return null;
		if (r.getMaxDepth()<=d) return null;
		// now r.maxDepth>d
		if (!r.hasRChild()) {
			if (!r.hasLChild()) return r;
			if (r.getLChild().isMarked()) return r;
			if (r.getLChild().getMaxDepth()<=d) return r;
			r=r.getLChild();
		}else if (r.getRChild().isMarked() || r.getRChild().getMaxDepth()<=d){
			if (!r.hasLChild()) return r;
			if (r.getLChild().isMarked()) return r;
			if (r.getLChild().getMaxDepth()<=d) return r;
			r=r.getLChild();
		}else {
			r=r.getRChild();
		}
		return maxNodeDeeperThan(r, d);
	}
	// cut node within (lb,rb) from T tree, lb and rb are open boundary!!!
	// r is root of top aux tree
	public BiTreeNode cutWithinLR(BiTreeNode r, BiTreeNode lb,BiTreeNode rb) {
		ArrayList<BiTreeNode> a1,a2;
		BiTreeNode a,b,c,d,e;
		if (lb==null && rb==null) {
			return r;       // no nodes are cut from top aux tree
		}else {
			// r-> B + lb + C
			if (lb==null) {
				assert(rb!=null);
				b=null;
				c=r;
			}else {
				a1=AVLTree.split(r, lb); 
				b=a1.get(0);
				c=a1.get(2);
			}
			// C -> D + rb +E
			if (rb==null) {
				assert(lb!=null);
				d=c;
				e=null;
			}else {
				a2=AVLTree.split(c, rb); 
				d=a2.get(0);
				e=a2.get(2);
			}
			if (d==null) return r;
			// mark root of D
			d.setMark(true); 
			
			if (rb==null) {
				lb.attachR(d);
				//a=AVLTree.mergeTreeNode(b,lb);
				a=AVLTree.merge(b,lb,null);
				return a;
			}
			// rb + E -> C
			rb.attachL(d);
			// B + lb + C -> A
			if (lb==null) {
				//a=AVLTree.mergeNodeTree(rb, e);
				a=AVLTree.merge(null,rb, e);
				return a;
			}
			//c=AVLTree.mergeNodeTree(rb, e);
			c=AVLTree.merge(null,rb, e);
			a=AVLTree.merge(b,lb,c);
			return a;
		}
	};
	
	public BiTreeNode join(BiTreeNode a, BiTreeNode n) {  // a is upper tree, b is lower tree
		// 1st step, find range of n
		//search n.root in a, get x. if x<n, than n is between (x,x.successor)
		// Otherwise, n is between (x.predecessor,x)
		BiTreeNode lb,rb; // left and right open boundary
		BiTreeNode x=binsearchAux(a,n);
		assert(x.getKey()!=n.getKey());
		if (x.getKey()<n.getKey()) {
			lb=x; 
			rb=x.getSucc();
		}else {
			rb=x;
			lb=x.getPrev();
		}
		// 2nd step
		ArrayList<BiTreeNode> a1,a2;
		BiTreeNode b,c,e;
		if (lb==null) {
			a1=AVLTree.split(a,rb);
			e=a1.get(2);
			n.setMark(false);
			return AVLTree.merge(n, rb, e);
		}
		if (rb==null) {
			a1=AVLTree.split(a, lb);
			b=a1.get(0);
			n.setMark(false);
			return AVLTree.merge(b,lb,n);
		}
		// now lb!=null && rb!=null
		a1=AVLTree.split(a, lb);
		b=a1.get(0);
		c=a1.get(2);
		a2=AVLTree.split(c, rb);
		e=a2.get(2);
		n.setMark(false); // unmark the root of n
		return AVLTree.merge(b,lb,AVLTree.merge(n,rb,e));
	}
	// search node n in top aux tree r
	public BiTreeNode binsearchAux(BiTreeNode r, BiTreeNode n) {
		int k=n.getKey();
		if (r==null) return null;
		if (r.getKey()==k) return r;
		else if(k<r.getKey()) {
			//search is within aux tree, when meet marked node,stop
			if (!r.hasLChild()) return r;
			if (r.getLChild().isMarked()) return r;
			return binsearchAux(r.getLChild(),n);
		}else {
			if (!r.hasRChild()) return r;
			if (r.getRChild().isMarked()) return r;
			return binsearchAux(r.getRChild(),n);
		}
	}
	public BiTreeNode binsearchAux(BiTreeNode r, int k) {
		o++;
		if (r==null) return null;
		if (r.getKey()==k) return r;
		else if(k<r.getKey()) {
			//search is within aux tree, when meet marked node,stop
			if (!r.hasLChild()) return r;
			if (r.getLChild().isMarked()) return r;
			return binsearchAux(r.getLChild(),k);
		}else {
			if (!r.hasRChild()) return r;
			if (r.getRChild().isMarked()) return r;
			return binsearchAux(r.getRChild(),k);
		}
	}
	// search in tango tree, the search itself will change the structure of tree when cross a marked node
	// search the tree rooted at r
	// change the tree structure and return the target node
	public BiTreeNode searchTango(int key) {
		if (root==null) return null;
		BiTreeNode x=binsearchAux(root, key);
		if (x.getKey()==key) {
			root=cut(root,x);
			x.updateMaxDepth(); // important! update MaxDepth from the bottom of the newly formed top aux tree
			x.updateHeight();
			return x;
		}
		if (x.getKey()>key) {
			if (!x.hasLChild()) return null;
			assert(x.getLChild().isMarked());
			root=cut(root,x);
			x.updateMaxDepth();
			root=join(root,x.getLChild());
			return searchTango(key);
		}else {// x.getKey()<key
			if (!x.hasRChild()) return null;
			assert(x.getRChild().isMarked());
			root=cut(root,x);
			x.updateMaxDepth();
			root=join(root,x.getRChild());
			return searchTango(key);
		}
	}
}
