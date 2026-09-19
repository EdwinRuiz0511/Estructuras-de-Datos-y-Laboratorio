/**
 * Un elemento de la "prueba de inclusión" (Merkle Proof).
 * Representa el hash de un nodo hermano necesario para reconstruir
 * el camino hacia la raíz, junto con la posición que ocupa
 * respecto al nodo que estamos verificando.
 */
public class ProofElement {

    private final String siblingHash;
    private final boolean siblingIsLeft; // true = el hermano va a la izquierda al concatenar

    public ProofElement(String siblingHash, boolean siblingIsLeft) {
        this.siblingHash = siblingHash;
        this.siblingIsLeft = siblingIsLeft;
    }

    public String getSiblingHash() {
        return siblingHash;
    }

    public boolean isSiblingLeft() {
        return siblingIsLeft;
    }

    @Override
    public String toString() {
        String lado = siblingIsLeft ? "IZQUIERDA" : "DERECHA";
        return "  -> hermano (" + lado + "): " + siblingHash.substring(0, 12) + "...";
    }
}
