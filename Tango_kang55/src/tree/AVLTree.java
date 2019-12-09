package tree;

import java.util.ArrayList;
import java.util.Arrays;

public class AVLTree extends BSTree{
	
	public AVLTree() {super();}
	public AVLTree(BiTreeNode root) {super(root);}

	// insert
	public void insert(int key, Object value, boolean mark,int depthP) {
		o++;
		super.insert(key, value, mark, depthP);     // insert key to the leave
		root=rebalance(last.getParent(),root);   // 
		////
		//last.updateMaxDepth();
	}
	public BiTreeNode remove(int key) {
		o++;
		BiTreeNode v=super.remove(key);
		if (v!=null)     // if the node is in tree
			root=rebalance(last,root);
		////
		//last.updateMaxDepth();
		return v;
	}
	// rebalance node until root
	// start from node z, go up until root r, if node in the path is not balanced, rotate tree to make it balanced
	// return root
	public static BiTreeNode rebalance(BiTreeNode z, BiTreeNode r) {
		// z is starting node, r is root
		if (z==null) return r;
		while(true) {
			o++;
			if(isBalanced(z)==false) rotate(z);   // if z is not balanced, do rotation
			if(z.hasParent()==false) return z;  // until z is root, all nodes are balanced
			z=z.getParent();
		}
	}
	// whether node n is balanced
	public static boolean isBalanced(BiTreeNode n) {
		if (n==null) return true;
		int lh=(n.hasLChild())?(n.getLChild().getHeight()):-1;
		int rh=(n.hasRChild())?(n.getRChild().getHeight()):-1;
		return (-1<=(lh-rh)) && ((lh-rh)<=1);
	}
	// four cases for AVL: use single or double-rotation to make system balance
	// return new root after rotation
	protected static BiTreeNode rotate(BiTreeNode z) {
		o++;
		BiTreeNode y=taller(z); // higher child of z
		BiTreeNode x=taller(y);
		boolean lc=z.isLChild();
		BiTreeNode p=z.getParent();
		BiTreeNode a,b,c;  // upper 3 nodes
		BiTreeNode t0,t1,t2,t3;  // attached 4 subtree
		// map x,y,z to a,b,c
		if (y.isLChild()) {  //zig
			c = z; t3 = z.getRChild();
			if (x.isLChild()) {
				b = y; t2 = y.getRChild();
				a = x; t1 = x.getRChild(); t0 = x.getLChild();
			} 
			else {  //zag+zig
				a = y; t0 = y.getLChild();
				b = x; t1 = x.getLChild(); t2 = x.getRChild();
			}
		} 
		else { //zag
			a = z; t0 = z.getLChild();
			if (x.isRChild()) {
				b = y; t1 = y.getLChild();
				c = x; t2 = x.getLChild(); t3 = x.getRChild();
			} 
			else {  //zig+zag
				c = y; t3 = y.getRChild();
				b = x; t1 = x.getLChild(); t2 = x.getRChild();
			}
		}
		// isolate x,y,z and t0~t3
		z.secede();
		y.secede();
		x.secede();
		if (null != t0) t0.secede();
		if (null != t1) t1.secede();
		if (null != t2) t2.secede();
		if (null != t3) t3.secede();
		// reconnect
		a.attachL(t0); a.attachR(t1); 
		b.attachL(a); c.attachL(t2); 
		c.attachR(t3); b.attachR(c);
		// attach the newly balanced tree to root
		if (null != p) {
			if (lc) p.attachL(b);
			else p.attachR(b);
		}
		return b; // return the root of new balanced tree 
	}
	public static BiTreeNode taller(BiTreeNode n) {
		int lh=(n.hasLChild())?(n.getLChild().getHeight()):-1;
		int rh=(n.hasRChild())?(n.getRChild().getHeight()):-1;
		if (lh>rh) return n.getLChild();
		if (lh<rh) return n.getRChild();
		if (n.isLChild()) return n.getLChild();
		return n.getRChild();
	}
	
	// merge two AVL tree and a boundary node b into a AVL tree, 
	//all nodes in Tree x < b.key, all nodes in Tree y > b.key, b is boundary node
	// x,y here are root of tree
	// for simplicity, b is not included by tree x or y !!!
	// return root of new AVL tree x+b+y
	public static BiTreeNode merge(BiTreeNode x,  BiTreeNode b, BiTreeNode y) {
		o++;
		if (x==null && y==null) {
			return b;
		}
		if (x==null && y !=null) {
			y.secede();
			// find left most node in y
			BiTreeNode lm=y;
			while(lm.hasLChild()) lm=lm.getLChild();
			lm.attachL(b);
			y=rebalance(lm,y);
			return y;
		}
		if (x!=null && y==null) {
			x.secede();
			BiTreeNode rm=x;
			while(rm.hasRChild()) rm=rm.getRChild();
			rm.attachR(b);
			x=rebalance(rm,x);
			return x;
		}
		x.secede();
		y.secede();
		int h1=x.getHeight();
		int h2=y.getHeight();
		// |h1-h2|<=1, attach x to b's left side, attach y to b's right side, new tree is balanced
		if ((h1-h2)<=1 && (h1-h2)>=-1) {
			b.attachL(x);
			b.attachR(y);
			return b;
		}else if (h1-h2>1){
			// node c has same height as y
			BiTreeNode c=searchRightmostNodewithHeight(x, h2);  
			BiTreeNode cp=c.getParent();
			c.secede();
			b.attachL(c);
			b.attachR(y);
			cp.attachR(b);  
			// at least now nodes below cp is balanced, start from cp, go up until root x, 
			//balance any node who dare to be unbalanced
			x=rebalance(cp,x);
			return x;
		}else {  // symmetrical to the upper case
			BiTreeNode c=searchLeftmostNodewithHeight(y, h1);
			BiTreeNode cp=c.getParent();
			c.secede();
			b.attachL(x);
			b.attachR(c);
			cp.attachL(b);
			y=rebalance(cp,y);
			return y;
		}
	}
	// this is designed for x+b 
	// b can be a single node or a node attached a marked child, b>x
	// x is a tree
	// return AVL tree x+b
	/*
	public static BiTreeNode mergeTreeNode(BiTreeNode x,  BiTreeNode b) {
		// first extract the max node in x
		BiTreeNode xMax=x.findMax(x);
		AVLTree eTree= new AVLTree(x);
		eTree.remove(xMax.getKey());
		BiTreeNode xMax_copy=new BiTreeNode(xMax.getE(), false, xMax.getDepthP());
		// merge x+x.max+b
		BiTreeNode a=merge(eTree.getRoot(),xMax_copy,b);
		return a;
	}*/
	// b<x, b is a single node or attached with a marked child
	/*
	public static BiTreeNode mergeNodeTree(BiTreeNode b,  BiTreeNode x) {
		// first extract the min node in x
		BiTreeNode xMin=x.findMin(x);
		AVLTree eTree= new AVLTree(x);
		eTree.remove(xMin.getKey());
		BiTreeNode xMin_copy=new BiTreeNode(xMin.getE(), false, xMin.getDepthP());
		// merge b+x.min+x
		BiTreeNode a=merge(b,xMin_copy,eTree.getRoot());
		return a;
	}*/
	// start from root r, go along the right path, return node c with height h
	// in our code, we already make sure r.height-h>1
	public static BiTreeNode searchRightmostNodewithHeight(BiTreeNode r, int h) {
		if (r==null) return null;
		if(r.getHeight()<h) return null; 
		else {
			while(true) {
				o++;
				if (r.getHeight()==h) break;
				if (r.getHeight()==1 && r.hasRChild()==false) break;  // it means h==0
				if (r.getHeight()==h+1 && r.getRChild().getHeight()==h-1) break;
				r=r.getRChild();  // go along the right path
			}
			return r;
		}
	}
	// start from root r, go along left path, return node c with height h
	// here, we are certain that r.height-h>1
	public static BiTreeNode searchLeftmostNodewithHeight(BiTreeNode r, int h) {
		if (r==null) return null;
		if(r.getHeight()<h) return null; 
		else {
			while(true) {
				o++;
				if (r.getHeight()==h) break;
				if (r.getHeight()==1 && r.hasLChild()==false) break;  // it means h==0
				if (r.getHeight()==h+1 && r.getLChild().getHeight()==h-1) break;
				r=r.getLChild();  // go along left path
			}
			return r;
		}
	}
	// split AVL tree to two AVL trees, all nodes in one tree are smaller than key b.key, 
	// nodes in another tree are larger than b.key
	// for simplicity, boundary bode b can be found in tree r !!!
	// return AVLTree left + node b + AVLTree right  
	public static ArrayList<BiTreeNode> split(BiTreeNode r, BiTreeNode b){
		o++;
		BiTreeNode left=null,right=null,t,tpl,tpr,s,t_copy,s_copy;
		t=BSTree.binSearch(r, b.getKey());
		if (t==null) return null;
		if (t.hasLChild()) {
			left=t.getLChild();		
			left.secede();
		}
		if (t.hasRChild()){
			right=t.getRChild();
			right.secede();
		}
		t_copy=new BiTreeNode(t.getE(), null, null,null, true, t.isMarked(), t.getDepthP());
		if (!t.hasParent()) return new ArrayList<BiTreeNode>(Arrays.asList(left,t_copy,right));
		
		while (true) {
			s=t.getParent();
			s_copy=new BiTreeNode(s.getE(), null, null,null, true, s.isMarked(), s.getDepthP());	

			if (t.isRChild()) {				
				tpl=s.getLChild();
				if (tpl!=null) tpl.secede();
				left=merge(tpl,s_copy,left);
			}
			else {
				assert(t.isLChild());
				tpr=s.getRChild();
				if (tpr!=null) tpr.secede();
				right=merge(right, s_copy, tpr);
			}
			t=s;
			if (!t.hasParent()) break;
		}

		return new ArrayList<BiTreeNode>(Arrays.asList(left,b,right));
	}
	/*
	// using recursive method, not sure where is wrong
	public static ArrayList<BiTreeNode> split(BiTreeNode r, BiTreeNode b) {
		o++;
		BiTreeNode left=null,right=null,rl,rr;
		if (b==null) {
			left=r;
			right=null;
		}
		else {
			int k=b.getKey();
			ArrayList<BiTreeNode> a;
			if (r==null) {
				left=null;right=null;
			}else{
				if (r.hasLChild())
					rl=r.getLChild();
				else
					rl=null;
				if (r.hasRChild())
					rr=r.getRChild();
				else
					rr=null;		
		
				if (rl!=null)
					rl.secede();
				if (rr!=null)
					rr.secede();
				if (k==r.getKey()){
					left = rl; 
					right = rr;
				}else if (k<r.getKey()) {
					a = split(rl,b);
					left = a.get(0);
					right=merge(a.get(2),r,rr);
				}else {
					a = split(rr,b);
					left=merge(rl,r,a.get(0));
					right=split(rr,b).get(2);
				}
			}
		}
		return new ArrayList<BiTreeNode>(Arrays.asList(left,b,right));
	}*/
}
