package tree;

import java.util.ArrayList;
import java.util.Random;

public class Test {
	// generalize random AVL tree with n nodes
	public static AVLTree initAVL(int n) {
		Random r=new Random();
		AVLTree t=new AVLTree();
		for (int i=0;i<n;i++)
			t.insert(r.nextInt(10000),0, false,0);
		return t;
	}
	public static void AVLTree_split_test(int n) {
		// split AVL to two AVL with a connection node
		AVLTree t=initAVL(n);
		//System.out.println("original tree: "+t.toString());
		//System.out.println("size of original tree: "+t.getSize());
		ArrayList<BiTreeNode> a=t.elementsInorder();
		
		BSTree.resetO();  // start from thsi step, count cost
		ArrayList<BiTreeNode> result=AVLTree.split(t.getRoot(), a.get(n/3));
		//System.out.println("splited 1st tree: "+new AVLTree(result.get(0)).toString());
		//System.out.println("size of 1st tree: "+result.get(0).getSize());
		//System.out.println("splited 2nd tree: "+new AVLTree(result.get(2)).toString());
		//System.out.println("size of 2nd tree: "+result.get(2).getSize());
		System.out.println("Cost Counter o:"+AVLTree.o);
	}
	public static void AVLTree_merge_test(int n1, int n2) {
		// merge two AVL with a node to AVL tree
		Random rnd=new Random();
		AVLTree x=new AVLTree();
		for (int i=0;i<n1;i++)
			x.insert(rnd.nextInt(1000),0,false,0);
		AVLTree y=new AVLTree();
		for (int i=0;i<n2;i++)
			y.insert(rnd.nextInt(1000)+1000,0,false,0);
		//System.out.println(x.toString());
		//System.out.println("size of 1st tree: "+x.getSize());
		//System.out.println(y.toString());
		//System.out.println("size of 2nd tree: "+y.getSize());
		BiTreeNode b=new BiTreeNode(1500,0,false);
		//System.out.println("connection node: "+b.getKey());
		
		BSTree.resetO();
		BiTreeNode r=AVLTree.merge(x.getRoot(),  b, y.getRoot());

		//System.out.println("combined new tree: "+new AVLTree(r).toString());
		//System.out.println("size of combined tree: "+r.getSize());

		System.out.println("Cost Counter o:"+AVLTree.o);
	}
	public static void TangoTree_search_test(int n) {
		TangoAVLTree t=TangoAVLTree.initializeTangoTree(n);
		BiTreeNode x;
		ArrayList<BiTreeNode> a=t.elementsInorder();
		//System.out.println("original tree: "+t.toString());
		System.out.println("original tree size: "+t.getSize());

		x = t.searchTango(a.get(n/2).getKey());
		//System.out.println("organized tree size: "+t.getSize());
		x = t.searchTango(a.get(n/4).getKey());
		//System.out.println("organized tree size: "+t.getSize());
		BSTree.resetO();
		x = t.searchTango(a.get(1).getKey());
		System.out.println("Cost Counter o:"+AVLTree.o);
	}
	public static void main(String[] args) {				
		AVLTree_split_test(50000);
		
		//AVLTree_merge_test(50, 20000);
		
		//TangoTree_search_test(50000);


	}
}
