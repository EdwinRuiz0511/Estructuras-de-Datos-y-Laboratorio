import experimento.EscritorCSV;
import experimento.Experimento;
import java.io.IOException;

public class Principal {

    // "throws IOException" porque abrir el archivo CSV puede fallar.
    public static void main(String[] args) throws IOException {

        // ---------- CONFIGURACIÓN (corrida real) ----------
        int[] valoresN = {100, 500, 1000, 5000, 10000, 50000, 100000};
        int m = 1000;
        int repeticionesMedidas = 30;
        int repeticionesCalentamiento = 3;
        int capacidadBMas = 32;
        long semillaBase = 1000;
        String rutaArchivo = "datos/resultados/resultados_busqueda.csv";
        // --------------------------------------------------

        EscritorCSV escritor = new EscritorCSV(rutaArchivo);
        Experimento experimento = new Experimento(escritor, capacidadBMas,
                repeticionesCalentamiento);

        long inicioTotal = System.nanoTime();
        for (int i = 0; i < valoresN.length; i++) {
            System.out.println("Iniciando N=" + valoresN[i]);
            experimento.ejecutarParaTamano(valoresN[i], m, repeticionesMedidas, semillaBase);
            escritor.guardar();
            System.out.println("Terminado N=" + valoresN[i]);
        }
        escritor.cerrar();

        long segundos = (System.nanoTime() - inicioTotal) / 1000000000L;
        System.out.println("Experimento terminado en " + segundos + " segundos.");
        System.out.println("Resultados en: " + rutaArchivo);
    }
}