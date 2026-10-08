package abb;

import modelo.Estudiante;

public class NodoABB {

    // El estudiante que guarda este nodo.
    public Estudiante estudiante;

    // Hijo izquierdo (IDs menores) e hijo derecho (IDs mayores).
    // Valen null si no hay hijo en ese lado.
    public NodoABB izquierdo;
    public NodoABB derecho;

    public NodoABB(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.izquierdo = null;
        this.derecho = null;
    }
}