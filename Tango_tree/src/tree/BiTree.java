package tree;

import java.util.ArrayList;

public interface BiTree {
	public BiTreeNode getRoot();
	public boolean isEmpty();
	public int getSize();
	public int getHeight();
	public ArrayList<BiTreeNode> elementsInorder();
}
