# AVL-based Tango Tree

In a binary search tree (BST), the best performance of the search can be realized when the tree is balanced. For example, in the AVL tree or red-black tree, the height of the tree is O(logn). So the search operation costs O(logn). Since we do not move the recently searched node closer to the root, it will cost another O(logn) to access the node again from the root. In the AVL or RB tree, the touched node has no priority to the root.

However, In the real application, the node previously searched is more likely to be searched again. To fully make use of this property, splay tree restructures a tree so that more frequently accessed nodes are brought closer to the root.

Tango tree makes use of this property in a different way.  In the following discussion, we will bring in two trees: static tree P and tango tree T. 
P is the normal BST, not necessarily balanced. P does not appear in the code and is just for simplification of understanding. We do not store P.
T is the tango tree we need to manipulate, and T is a reconstructed version of P. The operation such as “cut” is complicated in T but simple in P. we will first discuss these operations in P and then generalize them to T.

The details are discussed in the attached report. 

## Structures and operations

Below shows an example of clustering of tree P.

![Picture1](https://user-images.githubusercontent.com/31739574/97135377-13963180-171e-11eb-95cb-2d5417ea3f53.png)

Two operations (cut, join) related to this process:

![Picture2](https://user-images.githubusercontent.com/31739574/97135399-227ce400-171e-11eb-993d-695fda34ccf2.png)
