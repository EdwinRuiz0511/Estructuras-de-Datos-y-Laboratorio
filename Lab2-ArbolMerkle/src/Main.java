import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Forzar UTF-8 en la salida para que tildes y símbolos se muestren bien
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        separador("1. CREACIÓN DE 5 TRANSACCIONES SIMULADAS");
        List<String> transacciones = new ArrayList<>();
        transacciones.add("TX1: Edwin paga 50000 a Carlos");
        transacciones.add("TX2: Carlos paga 20000 a Maria");
        transacciones.add("TX3: Maria paga 15000 a Edwin");
        transacciones.add("TX4: Edwin paga 100000 a Julian");
        transacciones.add("TX5: Julian paga 30000 a Carlos");

        for (int i = 0; i < transacciones.size(); i++) {
            System.out.println((i + 1) + ". " + transacciones.get(i));
        }

        separador("2. CONSTRUCCIÓN DEL ÁRBOL Y RAÍZ (MERKLE ROOT)");
        MerkleTree arbol = new MerkleTree(transacciones);
        System.out.println("Diagrama del árbol:\n");
        arbol.printTree();
        System.out.println("\nMerkle Root original:");
        System.out.println(arbol.getRoot());

        separador("3. MODIFICAR UNA TRANSACCIÓN -> LA RAÍZ DEBE CAMBIAR");
        List<String> transaccionesModificadas = new ArrayList<>(transacciones);
        String original = transaccionesModificadas.get(2);
        String modificada = "TX3: Maria paga 999999999 a Edwin"; // dato alterado
        transaccionesModificadas.set(2, modificada);

        System.out.println("Transacción original : " + original);
        System.out.println("Transacción alterada  : " + modificada);

        MerkleTree arbolModificado = new MerkleTree(transaccionesModificadas);
        System.out.println("\nMerkle Root original  : " + arbol.getRoot());
        System.out.println("Merkle Root modificado: " + arbolModificado.getRoot());

        if (!arbol.getRoot().equals(arbolModificado.getRoot())) {
            System.out.println("\n✔ Correcto: al cambiar UNA sola transacción, la raíz cambia por completo.");
        } else {
            System.out.println("\n✘ ERROR: la raíz no debería coincidir.");
        }

        separador("4. PRUEBA DE INCLUSIÓN PARA LA TRANSACCIÓN 3");
        int indiceTX3 = 2; // TX3 está en la posición 2 (0-based)
        List<ProofElement> proof = arbol.getProof(indiceTX3);

        System.out.println("Dato de la transacción 3: \"" + transacciones.get(indiceTX3) + "\"");
        System.out.println("Prueba de inclusión generada (" + proof.size() + " pasos):");
        for (ProofElement pe : proof) {
            System.out.println(pe);
        }

        separador("5. VERIFICACIÓN CON DATO CORRECTO");
        boolean esValida = MerkleTree.verifyProof(
                transacciones.get(indiceTX3),
                proof,
                arbol.getRoot()
        );
        System.out.println("Verificando TX3 original contra la Merkle Root...");
        System.out.println("Resultado: " + (esValida ? "VÁLIDA ✔ (la transacción SÍ pertenece al árbol)"
                                                        : "INVÁLIDA ✘"));

        separador("6. VERIFICACIÓN CON DATO INCORRECTO (DEBE FALLAR)");
        String datoFalso = "TX3: Maria paga 15000000 a Edwin"; // dato manipulado por un atacante
        boolean esValidaFalsa = MerkleTree.verifyProof(
                datoFalso,
                proof,
                arbol.getRoot()
        );
        System.out.println("Verificando un dato ALTERADO (\"" + datoFalso + "\") contra la misma Merkle Root...");
        System.out.println("Resultado: " + (esValidaFalsa ? "VÁLIDA ✔ (ERROR, no debería pasar)"
                                                            : "INVÁLIDA ✘ (correcto, la verificación falla como se esperaba)"));

        separador("FIN DEL EXPERIMENTO");
    }

    private static void separador(String titulo) {
        System.out.println("\n===================================================");
        System.out.println(titulo);
        System.out.println("===================================================");
    }
}
