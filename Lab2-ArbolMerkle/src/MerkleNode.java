/**
 * Representa un nodo dentro del Árbol de Merkle.
 * Puede ser una hoja (left == right == null) o un nodo interno.
 */
public class MerkleNode {

    private final String hash;
    private final MerkleNode left;
    private final MerkleNode right;

    public MerkleNode(String hash, MerkleNode left, MerkleNode right) {
        this.hash = hash;
        this.left = left;
        this.right = right;
    }

    public String getHash() {
        return hash;
    }

    public MerkleNode getLeft() {
        return left;
    }

    public MerkleNode getRight() {
        return right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    /** Devuelve una versión corta del hash, útil para mostrar el diagrama en consola. */
    public String shortHash() {
        return hash.substring(0, 8) + "...";
    }
}
