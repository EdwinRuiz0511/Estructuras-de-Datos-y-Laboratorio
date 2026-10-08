package bmas;

import modelo.Estudiante;

// Un solo tipo de nodo que sirve para hojas y para nodos internos.
public class NodoBMas {

    // true = hoja (guarda estudiantes), false = nodo interno (guarda señales).
    public boolean esHoja;

    // Cuántos IDs hay guardados ahora mismo en el nodo.
    public int cantidadClaves;

    // En una hoja: los IDs de los estudiantes.
    // En un nodo interno: las señales que dicen hacia qué hijo bajar.
    public int[] claves;

    // Solo en hojas: el estudiante de cada ID (misma posición que en claves).
    public Estudiante[] estudiantes;

    // Solo en nodos internos: los hijos. Hay un hijo más que señales.
    public NodoBMas[] hijos;

    // Solo en hojas: la hoja que sigue a la derecha (el enlace del dibujo).
    public NodoBMas siguiente;

    public NodoBMas(boolean esHoja, int capacidad) {
        this.esHoja = esHoja;
        this.cantidadClaves = 0;
        this.claves = new int[capacidad + 1];
        this.siguiente = null;

        if (esHoja) {
            this.estudiantes = new Estudiante[capacidad + 1];
            this.hijos = null;
        } else {
            this.estudiantes = null;
            this.hijos = new NodoBMas[capacidad + 2];
        }
    }
}