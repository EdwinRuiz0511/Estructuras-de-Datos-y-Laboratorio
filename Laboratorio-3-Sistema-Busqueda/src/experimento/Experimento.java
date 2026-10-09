package experimento;

import abb.ArbolABB;
import bmas.ArbolBMas;
import lista.ListaEstudiantes;
import modelo.Estudiante;

public class Experimento {

    private static final long DESPLAZAMIENTO_BUSQUEDAS = 100000;

    private MedidorTiempo medidor;
    private EscritorCSV escritor;
    private int capacidadBMas;
    private int repeticionesCalentamiento;

    private int n;
    private int m;
    private int repeticionActual;
    private long semillaActual;
    private boolean registrar;
    private Estudiante[] estudiantesMezclados;
    private Estudiante[] estudiantesOrdenados;
    private int[] idsBusqueda;

    public Experimento(EscritorCSV escritor, int capacidadBMas, int repeticionesCalentamiento) {
        this.medidor = new MedidorTiempo();
        this.escritor = escritor;
        this.capacidadBMas = capacidadBMas;
        this.repeticionesCalentamiento = repeticionesCalentamiento;
    }

    public void ejecutarParaTamano(int tamano, int cantidadBusquedas,
                                   int repeticionesMedidas, long semillaBase) {
        for (int r = 1; r <= repeticionesCalentamiento; r++) {
            ejecutarRepeticion(tamano, cantidadBusquedas, r, semillaBase - r, false);
            System.out.println("  N=" + tamano + " calentamiento "
                    + r + "/" + repeticionesCalentamiento);
        }

        for (int r = 1; r <= repeticionesMedidas; r++) {
            ejecutarRepeticion(tamano, cantidadBusquedas, r, semillaBase + r, true);
            System.out.println("  N=" + tamano + " repeticion "
                    + r + "/" + repeticionesMedidas);
        }
    }

    private void ejecutarRepeticion(int tamano, int cantidadBusquedas, int repeticion,
                                    long semilla, boolean guardarResultados) {
        this.n = tamano;
        this.m = cantidadBusquedas;
        this.repeticionActual = repeticion;
        this.semillaActual = semilla;
        this.registrar = guardarResultados;

        int[] idsMezclados = GeneradorDatos.generarIdsMezclados(n, semilla);
        int[] idsOrdenados = GeneradorDatos.generarIdsOrdenados(n);
        estudiantesMezclados = GeneradorDatos.generarEstudiantes(idsMezclados);
        estudiantesOrdenados = GeneradorDatos.generarEstudiantes(idsOrdenados);
        idsBusqueda = GeneradorBusquedas.generarIdsBusqueda(
                n, m, semilla + DESPLAZAMIENTO_BUSQUEDAS);

        medirLista();
        medirABB("aleatoria", estudiantesMezclados);
        medirABB("ordenada", estudiantesOrdenados);
        medirBMas("aleatoria", estudiantesMezclados);
        medirBMas("ordenada", estudiantesOrdenados);
    }

    private void medirLista() {
        ListaEstudiantes lista = new ListaEstudiantes();
        for (int i = 0; i < n; i++) {
            lista.insertar(estudiantesMezclados[i]);
        }
        System.gc();

        long tiempo = medidor.medirBusquedasLista(lista, idsBusqueda);
        guardarResultado("Lista", "aleatoria", 0, tiempo, 0);
    }

    private void medirABB(String tipoInsercion, Estudiante[] estudiantes) {
        ArbolABB arbol = new ArbolABB();
        for (int i = 0; i < n; i++) {
            arbol.insertar(estudiantes[i]);
        }
        System.gc();

        long tiempo = medidor.medirBusquedasABB(arbol, idsBusqueda);
        guardarResultado("ABB", tipoInsercion, 0, tiempo, arbol.obtenerAltura());
    }

    private void medirBMas(String tipoInsercion, Estudiante[] estudiantes) {
        ArbolBMas arbol = new ArbolBMas(capacidadBMas);
        for (int i = 0; i < n; i++) {
            arbol.insertar(estudiantes[i]);
        }
        System.gc();

        long tiempo = medidor.medirBusquedasBMas(arbol, idsBusqueda);
        guardarResultado("B+", tipoInsercion, capacidadBMas, tiempo, arbol.obtenerAltura());
    }

    private void guardarResultado(String estructura, String tipoInsercion,
                                  int capacidad, long tiempo, int altura) {
        int encontrados = medidor.getEncontradosUltimaMedicion();
        if (encontrados != m) {
            throw new IllegalStateException("Error: " + estructura + " " + tipoInsercion
                    + " encontro " + encontrados + " de " + m + " IDs");
        }
        if (registrar) {
            ResultadoExperimento resultado = new ResultadoExperimento(
                    "busqueda", estructura, tipoInsercion, n, m, capacidad,
                    repeticionActual, semillaActual, tiempo, altura, encontrados);
            escritor.escribirFila(resultado);
        }
    }
}