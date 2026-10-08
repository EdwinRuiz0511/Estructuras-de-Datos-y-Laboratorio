package lista;

import modelo.Estudiante;
import java.util.ArrayList;

public class ListaEstudiantes {

    // Aquí se guardan los estudiantes en el orden en que llegan.
    private ArrayList<Estudiante> estudiantes;

    public ListaEstudiantes() {
        estudiantes = new ArrayList<Estudiante>();
    }

    // Inserta al final de la lista. No revisa si el ID ya existe:
    // el generador de datos garantiza IDs únicos.
    public void insertar(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    // Búsqueda secuencial: revisa uno por uno desde el inicio.
    // Devuelve el estudiante, o null si el ID no existe.
    public Estudiante buscar(int idBuscado) {
        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante actual = estudiantes.get(i);
            if (actual.getId() == idBuscado) {
                return actual;
            }
        }
        return null;
    }

    public int cantidad() {
        return estudiantes.size();
    }

    // Devuelve los estudiantes ordenados por ID ascendente.
    // Ordena una COPIA, para no alterar el orden original de la lista.
    public Estudiante[] listar() {
        int cantidad = estudiantes.size();
        Estudiante[] copia = new Estudiante[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = estudiantes.get(i);
        }
        Estudiante[] auxiliar = new Estudiante[cantidad];
        ordenarPorId(copia, auxiliar, 0, cantidad - 1);
        return copia;
    }

    // Merge sort: divide el tramo en dos mitades, ordena cada una
    // y luego las mezcla.
    private void ordenarPorId(Estudiante[] arreglo, Estudiante[] auxiliar, int inicio, int fin) {
        if (inicio >= fin) {
            return; // un solo elemento ya está ordenado
        }
        int medio = (inicio + fin) / 2;
        ordenarPorId(arreglo, auxiliar, inicio, medio);
        ordenarPorId(arreglo, auxiliar, medio + 1, fin);
        mezclar(arreglo, auxiliar, inicio, medio, fin);
    }

    // Mezcla dos mitades ya ordenadas: [inicio..medio] y [medio+1..fin].
    private void mezclar(Estudiante[] arreglo, Estudiante[] auxiliar, int inicio, int medio, int fin) {
        // Se copia el tramo al auxiliar para poder sobrescribir el arreglo.
        for (int k = inicio; k <= fin; k++) {
            auxiliar[k] = arreglo[k];
        }

        int izquierda = inicio;
        int derecha = medio + 1;
        int posicion = inicio;

        // Se toma siempre el menor de los dos primeros elementos.
        while (izquierda <= medio && derecha <= fin) {
            if (auxiliar[izquierda].getId() <= auxiliar[derecha].getId()) {
                arreglo[posicion] = auxiliar[izquierda];
                izquierda++;
            } else {
                arreglo[posicion] = auxiliar[derecha];
                derecha++;
            }
            posicion++;
        }

        // Si sobraron elementos de la mitad izquierda, se copian.
        // Los de la derecha ya están en su lugar, no hace falta copiarlos.
        while (izquierda <= medio) {
            arreglo[posicion] = auxiliar[izquierda];
            izquierda++;
            posicion++;
        }
    }
}