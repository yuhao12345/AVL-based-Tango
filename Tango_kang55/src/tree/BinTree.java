package tree;

import java.util.ArrayList;

public class BinTree implements BiTree{
	protected BiTreeNode root;
	public BinTree() {root=null;}
	public BinTree(BiTreeNode r) {root=r;}
	public BiTreeNode getRoot() {
		return root;
	}
	public boolean isEmpty() {
		return null==root;
	}
	public int getSize() {
		return isEmpty() ? 0:root.getSize();
	}
	public int getHeight() {
		return isEmpty() ? -1:root.getHeight();
	}
	public ArrayList<BiTreeNode> elementsInorder() {
		return root.elementsInorder();
	}
}
