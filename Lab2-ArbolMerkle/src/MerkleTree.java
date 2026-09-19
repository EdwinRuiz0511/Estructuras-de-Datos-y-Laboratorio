import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Árbol de Merkle.
 *
 * Reglas de construcción:
 *  - Cada hoja = SHA-256(bloque de datos).
 *  - Cada nodo interno = SHA-256(hash(hijo izq) + hash(hijo der)).
 *  - Si en un nivel el número de nodos es impar, el último se duplica
 *    (se empareja consigo mismo) para poder seguir combinando de a pares.
 *  - La raíz (Merkle Root) es el único nodo del último nivel.
 */
public class MerkleTree {

    // levels.get(0) = hojas, levels.get(levels.size()-1) = [raíz]
    private final List<List<MerkleNode>> levels = new ArrayList<>();
    private final List<String> originalData;

    public MerkleTree(List<String> dataBlocks) {
        if (dataBlocks == null || dataBlocks.isEmpty()) {
            throw new IllegalArgumentException("Se necesita al menos un bloque de datos.");
        }
        this.originalData = new ArrayList<>(dataBlocks);
        build();
    }

    // ---------- Construcción del árbol ----------

    private void build() {
        // Nivel 0: hojas
        List<MerkleNode> currentLevel = new ArrayList<>();
        for (String data : originalData) {
            currentLevel.add(new MerkleNode(sha256(data), null, null));
        }
        levels.add(currentLevel);

        // Subir de nivel en nivel hasta llegar a la raíz
        while (currentLevel.size() > 1) {
            List<MerkleNode> nextLevel = new ArrayList<>();

            for (int i = 0; i < currentLevel.size(); i += 2) {
                MerkleNode left = currentLevel.get(i);
                // Si es impar, se duplica el último nodo del nivel
                MerkleNode right = (i + 1 < currentLevel.size())
                        ? currentLevel.get(i + 1)
                        : currentLevel.get(i);

                String parentHash = sha256(left.getHash() + right.getHash());
                nextLevel.add(new MerkleNode(parentHash, left, right));
            }

            levels.add(nextLevel);
            currentLevel = nextLevel;
        }
    }

    public String getRoot() {
        List<MerkleNode> topLevel = levels.get(levels.size() - 1);
        return topLevel.get(0).getHash();
    }

    public int leafCount() {
        return originalData.size();
    }

    // ---------- Prueba de inclusión (Merkle Proof) ----------

    /**
     * Genera la prueba de inclusión para la hoja en la posición "index" (0-based).
     * La prueba es la lista de hashes hermanos necesarios para reconstruir la raíz.
     */
    public List<ProofElement> getProof(int index) {
        if (index < 0 || index >= originalData.size()) {
            throw new IndexOutOfBoundsException("Índice de transacción inválido: " + index);
        }

        List<ProofElement> proof = new ArrayList<>();
        int idx = index;

        // Recorremos todos los niveles excepto el último (la raíz no tiene hermano)
        for (int level = 0; level < levels.size() - 1; level++) {
            List<MerkleNode> levelNodes = levels.get(level);

            boolean isRightNode = (idx % 2 == 1);
            int siblingIdx = isRightNode ? idx - 1 : idx + 1;

            // Caso de duplicado: si el hermano no existe, el hermano es el mismo nodo
            if (siblingIdx >= levelNodes.size()) {
                siblingIdx = idx;
            }

            MerkleNode sibling = levelNodes.get(siblingIdx);
            // Si el nodo actual es el derecho, el hermano va a la izquierda al concatenar
            proof.add(new ProofElement(sibling.getHash(), isRightNode));

            idx = idx / 2;
        }

        return proof;
    }

    /**
     * Verifica que "data" pertenezca al árbol cuya raíz es "expectedRoot",
     * usando la prueba de inclusión "proof". No necesita el árbol completo,
     * solo el dato original y la prueba: así funciona un Merkle Proof real.
     */
    public static boolean verifyProof(String data, List<ProofElement> proof, String expectedRoot) {
        String computedHash = sha256(data);

        for (ProofElement element : proof) {
            if (element.isSiblingLeft()) {
                computedHash = sha256(element.getSiblingHash() + computedHash);
            } else {
                computedHash = sha256(computedHash + element.getSiblingHash());
            }
        }

        return computedHash.equals(expectedRoot);
    }

    // ---------- Utilidades ----------

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    /** Imprime el árbol nivel por nivel (de la raíz hacia las hojas), en ASCII. */
    public void printTree() {
        for (int level = levels.size() - 1; level >= 0; level--) {
            String etiqueta = (level == levels.size() - 1) ? "RAÍZ   " :
                               (level == 0) ? "HOJAS  " : "NIVEL " + level + " ";
            StringBuilder sb = new StringBuilder(etiqueta + ": ");
            for (MerkleNode node : levels.get(level)) {
                sb.append("[").append(node.shortHash()).append("] ");
            }
            System.out.println(sb.toString());
        }
    }
}
