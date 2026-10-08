package bmas;

import modelo.Estudiante;

public class ArbolBMas {

    // Máximo de IDs por nodo. Se decide al crear el árbol.
    private int capacidad;

    private NodoBMas raiz;
    private int cantidad;

    // Número de niveles. Árbol con una sola hoja = 1.
    private int altura;

    // "Mensajeros" de una división: cuando un nodo se parte, aquí se deja
    // la señal que debe subir y el nodo nuevo que quedó a la derecha.
    private int senalAscendente;
    private NodoBMas nodoNuevoDerecho;

    public ArbolBMas(int capacidad) {
        this.capacidad = capacidad;
        this.raiz = null;
        this.cantidad = 0;
        this.altura = 0;
    }

    public int cantidad() {
        return cantidad;
    }

    public int obtenerAltura() {
        return altura;
    }

    // Dice por cuál hijo bajar para encontrar un ID.
    // Si el ID es igual a una señal, se va a la derecha.
    private int encontrarIndiceHijo(NodoBMas nodo, int id) {
        int indice = 0;
        while (indice < nodo.cantidadClaves && id >= nodo.claves[indice]) {
            indice++;
        }
        return indice;
    }

    // Busca por ID: baja por los nodos internos hasta una hoja y
    // allí revisa los IDs uno por uno.
    public Estudiante buscar(int idBuscado) {
        if (raiz == null) {
            return null;
        }

        NodoBMas actual = raiz;
        while (actual.esHoja == false) {
            int indice = encontrarIndiceHijo(actual, idBuscado);
            actual = actual.hijos[indice];
        }

        for (int i = 0; i < actual.cantidadClaves; i++) {
            if (actual.claves[i] == idBuscado) {
                return actual.estudiantes[i];
            }
        }
        return null;
    }

    // Devuelve los estudiantes ordenados por ID ascendente.
    // Va a la primera hoja y sigue los enlaces "siguiente".
    public Estudiante[] listar() {
        Estudiante[] resultado = new Estudiante[cantidad];
        if (raiz == null) {
            return resultado;
        }

        // Bajar siempre por el primer hijo hasta la hoja de más a la izquierda.
        NodoBMas hoja = raiz;
        while (hoja.esHoja == false) {
            hoja = hoja.hijos[0];
        }

        // Recorrer las hojas de izquierda a derecha.
        int indice = 0;
        while (hoja != null) {
            for (int i = 0; i < hoja.cantidadClaves; i++) {
                resultado[indice] = hoja.estudiantes[i];
                indice++;
            }
            hoja = hoja.siguiente;
        }
        return resultado;
    }

    // Inserta un estudiante. Si la raíz se parte, el árbol crece un nivel.
    public void insertar(Estudiante estudiante) {
        if (raiz == null) {
            raiz = new NodoBMas(true, capacidad);
            altura = 1;
        }

        boolean raizSeDividio = insertarEnNodo(raiz, estudiante);
        cantidad++;

        if (raizSeDividio) {
            // Se crea una raíz nueva con una sola señal y dos hijos.
            NodoBMas nuevaRaiz = new NodoBMas(false, capacidad);
            nuevaRaiz.claves[0] = senalAscendente;
            nuevaRaiz.hijos[0] = raiz;
            nuevaRaiz.hijos[1] = nodoNuevoDerecho;
            nuevaRaiz.cantidadClaves = 1;
            raiz = nuevaRaiz;
            altura++;
        }
    }

    // Inserta dentro de un nodo. Devuelve true si el nodo se partió
    // (en ese caso deja los datos de la división en los "mensajeros").
    private boolean insertarEnNodo(NodoBMas nodo, Estudiante estudiante) {
        if (nodo.esHoja) {
            return insertarEnHoja(nodo, estudiante);
        }

        // Nodo interno: se baja por el hijo que corresponde.
        int indiceHijo = encontrarIndiceHijo(nodo, estudiante.getId());
        boolean hijoSeDividio = insertarEnNodo(nodo.hijos[indiceHijo], estudiante);

        if (hijoSeDividio == false) {
            return false;
        }

        // El hijo se partió: hay que meter la señal y el hijo nuevo aquí.
        // Primero se abre un hueco corriendo señales e hijos a la derecha.
        for (int i = nodo.cantidadClaves; i > indiceHijo; i--) {
            nodo.claves[i] = nodo.claves[i - 1];
            nodo.hijos[i + 1] = nodo.hijos[i];
        }
        nodo.claves[indiceHijo] = senalAscendente;
        nodo.hijos[indiceHijo + 1] = nodoNuevoDerecho;
        nodo.cantidadClaves++;

        if (nodo.cantidadClaves <= capacidad) {
            return false; // todavía cabe, no hay división
        }
        return dividirNodoInterno(nodo);
    }

    // Mete el estudiante en la hoja manteniéndola ordenada por ID.
    private boolean insertarEnHoja(NodoBMas hoja, Estudiante estudiante) {
        int id = estudiante.getId();

        // Se corren a la derecha los IDs mayores para abrir el hueco.
        int posicion = hoja.cantidadClaves;
        while (posicion > 0 && hoja.claves[posicion - 1] > id) {
            hoja.claves[posicion] = hoja.claves[posicion - 1];
            hoja.estudiantes[posicion] = hoja.estudiantes[posicion - 1];
            posicion--;
        }
        hoja.claves[posicion] = id;
        hoja.estudiantes[posicion] = estudiante;
        hoja.cantidadClaves++;

        if (hoja.cantidadClaves <= capacidad) {
            return false; // todavía cabe, no hay división
        }
        return dividirHoja(hoja);
    }

    // Parte una hoja que quedó con capacidad + 1 IDs.
    // La mitad derecha pasa a una hoja nueva y su primer ID se COPIA arriba.
    private boolean dividirHoja(NodoBMas hoja) {
        int total = hoja.cantidadClaves;
        int cantidadIzquierda = total / 2;

        NodoBMas hojaNueva = new NodoBMas(true, capacidad);
        int j = 0;
        for (int i = cantidadIzquierda; i < total; i++) {
            hojaNueva.claves[j] = hoja.claves[i];
            hojaNueva.estudiantes[j] = hoja.estudiantes[i];
            hoja.estudiantes[i] = null;
            j++;
        }
        hojaNueva.cantidadClaves = total - cantidadIzquierda;
        hoja.cantidadClaves = cantidadIzquierda;

        // Se mantienen enlazadas las hojas.
        hojaNueva.siguiente = hoja.siguiente;
        hoja.siguiente = hojaNueva;

        // El primer ID de la hoja nueva se copia arriba (también se queda en la hoja).
        senalAscendente = hojaNueva.claves[0];
        nodoNuevoDerecho = hojaNueva;
        return true;
    }

    // Parte un nodo interno que quedó con capacidad + 1 señales.
    // La señal del medio SUBE y se va del nodo.
    private boolean dividirNodoInterno(NodoBMas nodo) {
        int total = nodo.cantidadClaves;
        int medio = total / 2;

        NodoBMas nodoNuevo = new NodoBMas(false, capacidad);

        // Las señales a la derecha del medio pasan al nodo nuevo.
        int j = 0;
        for (int i = medio + 1; i < total; i++) {
            nodoNuevo.claves[j] = nodo.claves[i];
            j++;
        }

        // Los hijos a la derecha del medio también pasan al nodo nuevo.
        j = 0;
        for (int i = medio + 1; i <= total; i++) {
            nodoNuevo.hijos[j] = nodo.hijos[i];
            nodo.hijos[i] = null;
            j++;
        }

        nodoNuevo.cantidadClaves = total - medio - 1;
        senalAscendente = nodo.claves[medio];
        nodo.cantidadClaves = medio;
        nodoNuevoDerecho = nodoNuevo;
        return true;
    }
}