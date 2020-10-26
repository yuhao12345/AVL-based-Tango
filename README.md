In a binary search tree (BST), the best performance of the search can be realized when the tree is balanced. For example, in the AVL tree or red-black tree, the height of the tree is O(logn). So the search operation costs O(logn). Since we do not move the recently searched node closer to the root, it will cost another O(logn) to access the node again from the root. In the AVL or RB tree, the touched node has no priority to the root.

However, In the real application, the node previously searched is more likely to be searched again. To fully make use of this property, splay tree restructures a tree so that more frequently accessed nodes are brought closer to the root.

Tango tree makes use of this property in a different way.  In the following discussion, we will bring in two trees: static tree P and tango tree T. 
P is the normal BST, not necessarily balanced. P does not appear in the code and is just for simplification of understanding. We do not store P.
T is the tango tree we need to manipulate, and T is a reconstructed version of P. The operation such as “cut” is complicated in T but simple in P. we will first discuss these operations in P and then generalize them to T.
