package abb;

import modelo.Estudiante;

public class ArbolABB {

    // La raíz es el nodo de arriba. Vale null si el árbol está vacío.
    private NodoABB raiz;

    // Cuántos estudiantes hay guardados.
    private int cantidad;

    // Número de niveles del camino más largo desde la raíz.
    // Árbol vacío = 0, solo la raíz = 1.
    private int altura;

    public ArbolABB() {
        raiz = null;
        cantidad = 0;
        altura = 0;
    }

    public int cantidad() {
        return cantidad;
    }

    public int obtenerAltura() {
        return altura;
    }

    // Inserta un estudiante bajando desde la raíz hasta un lugar vacío.
    public void insertar(Estudiante estudiante) {
        NodoABB nodoNuevo = new NodoABB(estudiante);

        // Caso 1: el árbol está vacío, el nodo nuevo es la raíz.
        if (raiz == null) {
            raiz = nodoNuevo;
            cantidad++;
            altura = 1;
            return;
        }

        // Caso 2: se baja por el árbol hasta encontrar dónde colgarlo.
        NodoABB actual = raiz;
        int nivelActual = 1; // la raíz está en el nivel 1
        boolean insertado = false;
        while (insertado == false) {
            if (estudiante.getId() < actual.estudiante.getId()) {
                // Va a la izquierda.
                if (actual.izquierdo == null) {
                    actual.izquierdo = nodoNuevo;
                    insertado = true;
                } else {
                    actual = actual.izquierdo;
                    nivelActual++;
                }
            } else {
                // Va a la derecha.
                if (actual.derecho == null) {
                    actual.derecho = nodoNuevo;
                    insertado = true;
                } else {
                    actual = actual.derecho;
                    nivelActual++;
                }
            }
        }
        cantidad++;

        // El nodo nuevo quedó un nivel más abajo que su padre.
        int nivelNuevo = nivelActual + 1;
        if (nivelNuevo > altura) {
            altura = nivelNuevo;
        }
    }

    // Busca por ID bajando desde la raíz.
    // Devuelve el estudiante, o null si no existe.
    public Estudiante buscar(int idBuscado) {
        NodoABB actual = raiz;
        while (actual != null) {
            if (idBuscado == actual.estudiante.getId()) {
                return actual.estudiante;
            } else if (idBuscado < actual.estudiante.getId()) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }
        return null;
    }

    // Devuelve los estudiantes ordenados por ID ascendente (recorrido inorden).
    // Se usa una pila hecha con un arreglo para no usar recursión.
    public Estudiante[] listar() {
        Estudiante[] resultado = new Estudiante[cantidad];
        NodoABB[] pila = new NodoABB[cantidad];
        int tope = 0;      // cuántos nodos hay en la pila
        int indice = 0;    // próxima posición libre del resultado

        NodoABB actual = raiz;
        while (actual != null || tope > 0) {

            // Bajar todo lo posible por la izquierda, apilando.
            while (actual != null) {
                pila[tope] = actual;
                tope++;
                actual = actual.izquierdo;
            }

            // Sacar el de arriba de la pila: es el siguiente en orden.
            tope--;
            actual = pila[tope];
            resultado[indice] = actual.estudiante;
            indice++;

            // Pasar al hijo derecho y repetir.
            actual = actual.derecho;
        }
        return resultado;
    }
}