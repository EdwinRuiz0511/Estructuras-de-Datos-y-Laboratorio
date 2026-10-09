package experimento;

public class ResultadoExperimento {

    private final String operacion;
    private final String estructura;
    private final String tipoInsercion;
    private final int n;
    private final int m;
    private final int capacidad;
    private final int repeticion;
    private final long semilla;
    private final long tiempoNanosegundos;
    private final int altura;
    private final int encontrados;

    public ResultadoExperimento(String operacion, String estructura, String tipoInsercion,
                                int n, int m, int capacidad, int repeticion, long semilla,
                                long tiempoNanosegundos, int altura, int encontrados) {
        this.operacion = operacion;
        this.estructura = estructura;
        this.tipoInsercion = tipoInsercion;
        this.n = n;
        this.m = m;
        this.capacidad = capacidad;
        this.repeticion = repeticion;
        this.semilla = semilla;
        this.tiempoNanosegundos = tiempoNanosegundos;
        this.altura = altura;
        this.encontrados = encontrados;
    }

    // Convierte la fila en una línea de texto separada por comas.
    public String aLineaCSV() {
        return operacion + "," + estructura + "," + tipoInsercion + ","
                + n + "," + m + "," + capacidad + "," + repeticion + ","
                + semilla + "," + tiempoNanosegundos + "," + altura + "," + encontrados;
    }
}