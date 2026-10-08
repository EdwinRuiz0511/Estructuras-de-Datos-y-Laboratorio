package experimento;

import java.util.Random;

public class GeneradorBusquedas {

    // Genera m IDs al azar entre 1 y n (puede haber repetidos).
    // Con la misma semilla siempre salen los mismos IDs.
    public static int[] generarIdsBusqueda(int n, int m, long semilla) {
        Random aleatorio = new Random(semilla);
        int[] idsBusqueda = new int[m];
        for (int i = 0; i < m; i++) {
            idsBusqueda[i] = aleatorio.nextInt(n) + 1;
        }
        return idsBusqueda;
    }
}