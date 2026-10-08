import abb.ArbolABB;
import bmas.ArbolBMas;
import experimento.GeneradorBusquedas;
import experimento.GeneradorDatos;
import experimento.MedidorTiempo;
import lista.ListaEstudiantes;
import modelo.Estudiante;

public class Principal {
    public static void main(String[] args) {
        int n = 10000;
        int m = 1000;
        int repeticiones = 10;
        long semillaBase = 1000;

        long[] tiemposLista = new long[repeticiones];
        long[] tiemposAbbAleatorio = new long[repeticiones];
        long[] tiemposAbbOrdenado = new long[repeticiones];
        long[] tiemposBMas = new long[repeticiones];
        int[] alturasAbbAleatorio = new int[repeticiones];
        boolean todoCorrecto = true;

        MedidorTiempo medidor = new MedidorTiempo();

        // Estudiantes en orden creciente: son los mismos en todas las repeticiones.
        int[] idsOrdenados = GeneradorDatos.generarIdsOrdenados(n);
        Estudiante[] estudiantesOrdenados = GeneradorDatos.generarEstudiantes(idsOrdenados);

        for (int r = 0; r < repeticiones; r++) {
            long semilla = semillaBase + r;

            // 1. Preparar datos (NO se mide).
            int[] idsMezclados = GeneradorDatos.generarIdsMezclados(n, semilla);
            Estudiante[] estudiantesMezclados = GeneradorDatos.generarEstudiantes(idsMezclados);
            int[] idsBusqueda = GeneradorBusquedas.generarIdsBusqueda(n, m, semilla);

            // 2. Construir las estructuras (NO se mide).
            ListaEstudiantes lista = new ListaEstudiantes();
            ArbolABB abbAleatorio = new ArbolABB();
            ArbolABB abbOrdenado = new ArbolABB();
            ArbolBMas bMas = new ArbolBMas(32);
            for (int i = 0; i < n; i++) {
                lista.insertar(estudiantesMezclados[i]);
                abbAleatorio.insertar(estudiantesMezclados[i]);
                bMas.insertar(estudiantesMezclados[i]);
                abbOrdenado.insertar(estudiantesOrdenados[i]);
            }

            // 3. Sugerir limpieza de memoria (NO se mide).
            System.gc();

            // 4. Medir solo las búsquedas.
            tiemposLista[r] = medidor.medirBusquedasLista(lista, idsBusqueda);
            if (medidor.getEncontradosUltimaMedicion() != m) {
                todoCorrecto = false;
            }

            tiemposAbbAleatorio[r] = medidor.medirBusquedasABB(abbAleatorio, idsBusqueda);
            if (medidor.getEncontradosUltimaMedicion() != m) {
                todoCorrecto = false;
            }

            tiemposAbbOrdenado[r] = medidor.medirBusquedasABB(abbOrdenado, idsBusqueda);
            if (medidor.getEncontradosUltimaMedicion() != m) {
                todoCorrecto = false;
            }

            tiemposBMas[r] = medidor.medirBusquedasBMas(bMas, idsBusqueda);
            if (medidor.getEncontradosUltimaMedicion() != m) {
                todoCorrecto = false;
            }

            alturasAbbAleatorio[r] = abbAleatorio.obtenerAltura();
        }

        // Se imprime al final, fuera de cualquier medición.
        System.out.println("N=" + n + ", M=" + m + " (tiempos de las M busquedas, en microsegundos)");
        for (int r = 0; r < repeticiones; r++) {
            System.out.println("Rep " + (r + 1)
                    + " | lista=" + tiemposLista[r] / 1000
                    + " | ABB aleatorio=" + tiemposAbbAleatorio[r] / 1000
                    + " | ABB ordenado=" + tiemposAbbOrdenado[r] / 1000
                    + " | B+=" + tiemposBMas[r] / 1000
                    + " | altura ABB aleatorio=" + alturasAbbAleatorio[r]);
        }
        System.out.println("Todas las busquedas encontraron su ID: " + todoCorrecto);
    }
}