package experimento;

import modelo.Estudiante;
import java.util.Random;

public class GeneradorDatos {

    // IDs del 1 al n en orden creciente: 1, 2, 3, ..., n.
    public static int[] generarIdsOrdenados(int n) {
        int[] ids = new int[n];
        for (int i = 0; i < n; i++) {
            ids[i] = i + 1;
        }
        return ids;
    }

    // Los mismos IDs del 1 al n, pero en orden aleatorio.
    // Con la misma semilla siempre sale el mismo orden.
    public static int[] generarIdsMezclados(int n, long semilla) {
        int[] ids = generarIdsOrdenados(n);
        Random aleatorio = new Random(semilla);
        for (int i = n - 1; i > 0; i--) {
            int j = aleatorio.nextInt(i + 1);
            int temporal = ids[i];
            ids[i] = ids[j];
            ids[j] = temporal;
        }
        return ids;
    }

    // Crea un estudiante. Edad y promedio salen del ID,
    // así el mismo ID siempre tiene los mismos datos.
    public static Estudiante crearEstudiante(int id) {
        String nombre = "Estudiante" + id;
        int edad = 17 + (id % 19);           // entre 17 y 35
        double promedio = (id % 51) / 10.0;  // entre 0.0 y 5.0
        return new Estudiante(id, nombre, edad, promedio);
    }

    // Crea un estudiante por cada ID, en el mismo orden del arreglo de IDs.
    public static Estudiante[] generarEstudiantes(int[] ids) {
        Estudiante[] estudiantes = new Estudiante[ids.length];
        for (int i = 0; i < ids.length; i++) {
            estudiantes[i] = crearEstudiante(ids[i]);
        }
        return estudiantes;
    }
}